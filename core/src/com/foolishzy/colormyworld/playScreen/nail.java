package com.foolishzy.colormyworld.playScreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.item.staticItem;

/**
 * Created by foolishzy on 2016/2/25.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class nail {
    private boolean setToDestroy = false;
    private boolean isLive = true;
    private World world;
    private Body body;
    public nail(World world, MapObject object, boolean isSensor) {
        this.world = world;
        defineB2d( object,  isSensor);
    }

    private void defineB2d(MapObject object, boolean isSensor){
        if(RectangleMapObject.class.isAssignableFrom(object.getClass())){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //BodyDef
            BodyDef bdf = new BodyDef();
            bdf.position.set((rect.getX() + rect.getWidth() / 2 ) / ColorMyWorldGame.PPM,
                    (rect.getY() + rect.getHeight()/ 2) /ColorMyWorldGame.PPM);
            bdf.type = BodyDef.BodyType.StaticBody;
            //fixtureDef
            FixtureDef fixdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / ColorMyWorldGame.PPM,
                    rect.getHeight() / 2 / ColorMyWorldGame.PPM);
            fixdef.shape =  shape;
            fixdef.isSensor = isSensor;
            //create
            body = world.createBody(bdf);
            body.createFixture(fixdef);
        } else
            Gdx.app.log("mistake: ","init b2d nail");
    }

    public Body getBody(){
        return body;
    }

    public void update(){
        if (!isLive && setToDestroy){
            isLive = false;
            world.destroyBody(body);
        }
    }

    public void destroyBody(){
        if (!setToDestroy && isLive)
            setToDestroy = true;
        else
            Gdx.app.log("mistake: ","staticItem destroyBoy signal !!! ");
    }
}
