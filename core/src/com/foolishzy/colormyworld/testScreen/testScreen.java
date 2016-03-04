package com.foolishzy.colormyworld.testScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;


public class testScreen extends MyScreen{
    public testScreen(ColorMyWorldGame game) {
        super(game);

        new myBox(this);
        defineB2d();
    }
    private class myBox extends Sprite{
        private TextureRegion boxRegion;
        private World world;
        private testScreen screen;
        private Batch batch;
        private Body boxBody;

        public myBox(testScreen screen){
            this.screen = screen;
            this.batch = screen.batch;
            this.world = screen.world;
            defineyBox();

        }
        private void defineyBox(){
            boxRegion = new TextureRegion(new Texture("box.png"));
            BodyDef bdf = new BodyDef();
            FixtureDef fdf = new FixtureDef();
            bdf.type = BodyDef.BodyType.DynamicBody;
            bdf.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(20 / PPM, 20 / PPM);
            fdf.shape = shape;

            Body boxBody ;
            boxBody = world.createBody(bdf);
            boxBody.createFixture(fdf);
            setPosition(boxBody.getPosition().x - 20 / PPM, boxBody.getPosition().y - 20 / PPM);
            setBounds(getX(), getY(), 20 / PPM, 20 / PPM);
            setRegion(boxRegion);
            //ground
            bdf = new BodyDef();
            bdf.position.set(gamePort.getWorldWidth() / 2,
                    gamePort.getWorldHeight() / 2);
            bdf.type = BodyDef.BodyType.StaticBody;
            fdf = new FixtureDef();
            CircleShape circle = new CircleShape();
            circle.setPosition(new Vector2(gamePort.getWorldWidth() / 2,
                    gamePort.getWorldHeight() / 2));
            circle.setRadius(2 / PPM);
            fdf.shape = circle;
            Body cir = world.createBody(bdf);
            cir.createFixture(fdf);
            //joint
            RevoluteJointDef rejdf = new RevoluteJointDef();
            rejdf.initialize(cir, boxBody, cir.getPosition());
            world.createJoint(rejdf);
        }

        @Override
        public void draw(Batch batch) {
            super.draw(batch);
            update();
        }
    }
    private void defineB2d(){

        Body ground ;
        BodyDef bdf = new BodyDef();
        RevoluteJointDef rjdf = new RevoluteJointDef();
        RevoluteJoint rejoint ;
        FixtureDef fdf = new FixtureDef();

        //ground
        bdf.position.set(0,0);
        bdf.type = BodyDef.BodyType.StaticBody;
        EdgeShape shape = new EdgeShape();
        shape.set(new Vector2(0, 5 / PPM), new Vector2(gamePort.getWorldWidth(), 5 / PPM));
        fdf.shape = shape;
        fdf.density = 0.01f;
        ground = world.createBody(bdf);
        ground.createFixture(fdf);


        //left edge
        bdf = new BodyDef();
        bdf.position.set(0, gamePort.getWorldHeight() / 2);
        shape = new EdgeShape();
        shape.set(new Vector2(5 / PPM, 0), new Vector2(5 / PPM, gamePort.getWorldHeight()));
        bdf.type = BodyDef.BodyType.StaticBody;
        fdf = new FixtureDef();
        fdf.shape = shape;
        world.createBody(bdf).createFixture(fdf);
        //rotate
        //the first one
        bdf = new BodyDef();
        bdf.type = BodyDef.BodyType.DynamicBody;
        bdf.active = true;
        bdf.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2);

        fdf = new FixtureDef();
        fdf.density = 0.01f;
        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(10 / PPM, 100 / PPM);
        fdf.shape = shape1;
        Body body = world.createBody(bdf);
        body.createFixture(fdf);
        shape1.setAsBox(100 / PPM, 10 / PPM);
        fdf.shape = shape1;
        body.createFixture(fdf);

        //Revolute Joint
        rjdf.initialize(ground, body, ground.getPosition());
        rjdf.motorSpeed = MathUtils.PI / 6;
        rjdf.maxMotorTorque = 50000f;
        rjdf.enableMotor = true;
        rjdf.collideConnected = true;
        world.createJoint(rjdf);
    }

    @Override
    public float getPPM() {
        return super.getPPM();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2drender.render(world, gameCam.combined);
    }


    private void update(){
        world.step(1 / 50f, 6, 6);

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}