package com.foolishzy.colormyworld.playScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.item.staticItem;


/**
 * Created by foolishzy on 2016/2/16.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Bridge extends Sprite {
    private TextureRegion region;
    private bridgeScreen screen;
    private World world;
    private Joint joint;
    private TiledMap map;
    private Body bridgeBody;

    private nail rightNail;

    public Bridge(bridgeScreen screen) {
        //init basic
        this.screen = screen;
        this.map = screen.getMap();
        this.world = screen.getWorld();
        //define bridge
        defineBridge();

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

    }

    public void update(){
        //set LinearImpulse to keep it's position
        bridgeBody.applyLinearImpulse(new Vector2(0.1f, 0), bridgeBody.getWorldCenter(), true);

        //nail update
        rightNail.update();
    }

    private void defineBridge(){
        // bridge body
//        Body bridgeBody;
        BodyDef bdf = new BodyDef();
        bdf.type = BodyDef.BodyType.DynamicBody;
        MapObject object = map.getLayers().get(4).getObjects().get("bridge");
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        bdf.position.set((rect.getX() + rect.getWidth() / 2) / screen.getPPM(),
                (rect.getY() + rect.getHeight() / 2 ) / screen.getPPM()
        );
        bridgeBody = world.createBody(bdf);

                //bridge fixture
        FixtureDef fdf = new FixtureDef();
        fdf.density = 0.01f;
        fdf.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
        fdf.filter.maskBits = ColorMyWorldGame.PLAYER_BIT |
                ColorMyWorldGame.NAIL_BIT;
        PolygonShape shape  = new PolygonShape();
        shape.setAsBox(rect.getWidth() / screen.getPPM() / 2, rect.getHeight() / screen.getPPM() / 2);
        fdf.shape = shape;
        bridgeBody.createFixture(fdf);

        //rotate point
        Body rotatePointBody;
        bdf = new BodyDef();

        bdf.type = BodyDef.BodyType.StaticBody;

        object = screen.getMap().getLayers().get(4).getObjects().get("point");
        rect = ((RectangleMapObject) object).getRectangle();

        bdf.position.set(rect.getX() / screen.getPPM(),
                rect.getY() / screen.getPPM());
        rotatePointBody = world.createBody(bdf);

        //nails
        bdf = new BodyDef();
        bdf.type = BodyDef.BodyType.StaticBody;
        for(MapObject nail : map.getLayers().get(5).getObjects()) {
            if (nail.getProperties().containsKey("staticNail")) {
                new staticItem(world, object, false);
            } else rightNail = new nail(world, object, false);
            //when player catch the position ,destroy rightNail
        }

        /*
        ps:
            b2dBody's position is the center of it's fixture
            when we create joints , the method "initialize()"
            use the position of bodyA and boayB, so the anchor's
            position are the center of each body's fixture center.
         */

        //joint
        RevoluteJointDef rejdf = new RevoluteJointDef();
        rejdf.initialize(rotatePointBody, bridgeBody, rotatePointBody.getPosition());
//        rejdf.upperAngle = 3*MathUtils.PI / 2;
        rejdf.collideConnected = true;
//        rejdf.enableLimit = true;
//        rejdf.motorSpeed = MathUtils.PI / 6;
//        rejdf.maxMotorTorque = 100f;
        rejdf.enableMotor = true;
        joint = world.createJoint(rejdf);
    }

    public void rotateBridge(){
        rightNail.destroyBody();
    }

}

