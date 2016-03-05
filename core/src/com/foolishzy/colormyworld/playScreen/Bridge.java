package com.foolishzy.colormyworld.playScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.sun.media.jfxmediaimpl.MediaDisposer;


/**
 * Created by foolishzy on 2016/2/16.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Bridge extends Sprite  implements MediaDisposer.Disposable {
    private TextureRegion bridgeRegion;
    private bridgeScreen screen;
    private World world;
    private Joint joint;
    private TiledMap map;
    private Body bridgeBody;
    private Body rotatePointBody;
    private final short BRIDGE_BIT = ColorMyWorldGame.GROUND_BIT;
    private boolean isRotate;
    private staticItem fence;
    private Vector2 bridgeSize;
    private nail rightNail;

    public Bridge(bridgeScreen screen) {
        //init basic
        this.screen = screen;
        this.map = screen.getMap();
        this.world = screen.getWorld();

        isRotate = false;
        bridgeRegion = new TextureRegion(new Texture(Gdx.files.internal(
                "bridgeScreen/bridge.png")));

        //define bridge
        defineBridge();
        defineNail();
        defineFence();
        initRegion();

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void update(){
        //set LinearImpulse to keep it's position
            bridgeBody.applyLinearImpulse(new Vector2(0.00001f, 0f), bridgeBody.getWorldCenter(), true);
        //update region
         regionUpdate();
        //nail update
        rightNail.update();
        //fence update
        fence.update();

        //rotate bridge region
        if (isRotate){
            setRotation(bridgeBody.getAngle()*  180 / MathUtils.PI);
            if (bridgeBody.getAngle()* 180 / MathUtils.PI <= -89){
                isRotate = false;
            }
        }


    }

    private void initRegion(){
        setPosition(bridgeBody.getPosition().x - bridgeSize.x / 2,
                bridgeBody.getPosition().y - bridgeSize.y / 2);

        setBounds(getX(), getY(), bridgeSize.x, bridgeSize.y);

        setRegion(bridgeRegion);

        setOrigin(rotatePointBody.getPosition().x - getX(),
                rotatePointBody.getPosition().y - getY());
    }

    private void defineFence(){
        MapObject object = map.getLayers().get(6).getObjects().get("fence");
        fence = new staticItem(this.world, object, false);
    }

    private void defineBridge(){
        // bridge body

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
        fdf.filter.categoryBits = BRIDGE_BIT;
        fdf.filter.maskBits = ColorMyWorldGame.PLAYER_BIT |
                ColorMyWorldGame.NAIL_BIT;
        PolygonShape shape  = new PolygonShape();
        shape.setAsBox(rect.getWidth() / screen.getPPM() / 2, rect.getHeight() / screen.getPPM() / 2);
        fdf.shape = shape;
        bridgeBody.createFixture(fdf);

        //init bridgeSize
        bridgeSize = new Vector2(rect.getWidth() / screen.getPPM(),
                rect.getHeight() / screen.getPPM());

        //rotate point

        bdf = new BodyDef();

        bdf.type = BodyDef.BodyType.StaticBody;

        bdf.position.set((rect.getX() + rect.getWidth() / 2) / screen.getPPM(),
                rect.getY() / screen.getPPM());
        rotatePointBody = world.createBody(bdf);

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

        rejdf.collideConnected = true;

        rejdf.enableMotor = false;
        joint = world.createJoint(rejdf);
    }

    private void defineNail(){
       for (MapObject object : map.getLayers().get(5).getObjects()){
           if (object.getProperties().containsKey("temp")){
               //when player click the switch ,destroy the right nail to make bridge rotate
               rightNail = new nail(this.world, object, false, BRIDGE_BIT);
           }
           else
           //don't mention too much about this nails
            new nail(this.world, object, false, BRIDGE_BIT);
       }

    }

    public void rotateBridge(){
        rightNail.destroyBody();
        fence.destroyBody();
        isRotate = true;
        ((RevoluteJoint) joint).enableMotor(true);
    }

    public boolean isRotate(){
        return isRotate;
    }

    private void regionUpdate(){


    }

    @Override
    public void dispose() {
        world.destroyJoint(joint);
        world.destroyBody(bridgeBody);
        world.destroyBody(rotatePointBody);
        fence.dispose();
        rightNail.dispose();


    }
}

