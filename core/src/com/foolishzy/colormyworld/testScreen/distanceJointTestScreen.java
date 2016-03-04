package com.foolishzy.colormyworld.testScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;

/**
 * Created by foolishzy on 2016/3/4.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class distanceJointTestScreen extends MyScreen {
    //announce
    Body body1, body2;
    Vector2 Impulse;
    testBoard bd;
    float timer = 0;
    float count = 0;


    public distanceJointTestScreen(ColorMyWorldGame game) {
        super(game);

        Impulse = new Vector2(0.2f, 0f);
        bd = new testBoard(this);
        defineB2d();
    }

    private void defineB2d(){


        //body1
        BodyDef bdf = new BodyDef();
        bdf.type = BodyDef.BodyType.StaticBody;
        bdf.position.set(gameCam.position.x, gameCam.position.y);

        FixtureDef fdf = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / this.getPPM(), 50 / this.getPPM());
        fdf.shape = shape;

        body1 = world.createBody(bdf);
        body1.createFixture(fdf);


        //body2
        bdf = new BodyDef();
        bdf.type = BodyDef.BodyType.DynamicBody;
        bdf.position.set(gameCam.position.x, 0);

        fdf = new FixtureDef();
        shape = new PolygonShape();
        shape.setAsBox(25 / this.getPPM(), 25 / this.getPPM());
        fdf.shape = shape;

        body2 = world.createBody(bdf);
        body2.createFixture(fdf);

        //joint
        DistanceJointDef disJdf = new DistanceJointDef();
        disJdf.initialize(body1, body2, body1.getPosition(), body2.getPosition());
        disJdf.length = 200 / this.getPPM();

        world.createJoint(disJdf);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        bd.draw(batch);
        batch.end();

        b2drender.render(world, gameCam.combined);

    }

    private void update(float dt){
        //world step
        world.step(1 / 60f, 6, 4);

        //handle input
        handleInput(dt);

        bdupdate(dt);
    }

    private void bdupdate(float dt){

        if (count >= 1){
            count = 0;
            timer += 1;
        }
        else
            count += dt;

        bd.rotate(dt);
    }

    private void  handleInput(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            body2.applyLinearImpulse(Impulse, body2.getWorldCenter(), true);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public float getPPM() {
        return super.getPPM();
    }

    @Override
    public TiledMap getMap() {
        return super.getMap();
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    private class testBoard extends Sprite{
        Body testBody;
        TextureRegion boxImg;

        public  testBoard(MyScreen screen){
            boxImg = new TextureRegion(new Texture("box.png"));
            initB2d(screen);

            //set region

            setPosition(testBody.getPosition().x, testBody.getPosition().y);
            setBounds(getX(), getY(),
                    boxImg.getRegionWidth() / screen.getPPM(),
                    boxImg.getRegionHeight() / screen.getPPM());

//            setOrigin(testBody.getPosition().x, testBody.getPosition().y);
            setOrigin( boxImg.getRegionWidth() / 2 /screen.getPPM(),
                    boxImg.getRegionHeight() / 2 /screen.getPPM()
                    );
            setRegion(boxImg);

        }

        private void initB2d(MyScreen screen){
            BodyDef bdf = new BodyDef();
            bdf.position.set(gameCam.position.x, 50 / screen.getPPM() +
                    gameCam.position.y);
            bdf.type = BodyDef.BodyType.DynamicBody;

            FixtureDef fdf = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(50 / screen.getPPM(), 50 / screen.getPPM());
            fdf.shape = shape;

            testBody = world.createBody(bdf);
            testBody.createFixture(fdf);
        }

        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x, y);
        }

        @Override
        public void setRotation(float degrees) {
            super.setRotation(degrees);
        }

        @Override
        public void setOriginCenter() {
            super.setOriginCenter();
        }

        @Override
        public void draw(Batch batch) {
            super.draw(batch);
        }

        public Body getTestBody(){
            return testBody;
        }

    }
}
