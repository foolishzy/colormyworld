package com.foolishzy.colormyworld.preScreen.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.foolishzy.colormyworld.ColorMyWorldGame;

/**
 * Created by foolishzy on 2016/2/1.
 * <p>
 * funcction:
 * <p>
 * others:
 * ps: in this code , when the object is polygonShape or polyLineShape,
 *      there are some problem with it's position, i don't know how to
 *      set it's position,so in this game pls use rectangleShape
 */
public class staticItem {
    private boolean setToDestroy = false;
    private boolean isLive = true;
    private World world;
    private Body body;
    public staticItem(World world, MapObject object, boolean isSensor){
        this.world = world;
        //polyline object
        if (PolylineMapObject.class.isAssignableFrom(object.getClass())) {
            Polyline plyline = ((PolylineMapObject) object).getPolyline();
            float[] vertices = plyline.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] / ColorMyWorldGame.PPM;
            }
//            BodyDef
            BodyDef bdf = new BodyDef();
            bdf.position.set(plyline.getX() / ColorMyWorldGame.PPM, plyline.getY() / ColorMyWorldGame.PPM);
            bdf.type = BodyDef.BodyType.StaticBody;
//            fixtureDef
            FixtureDef fixdf = new FixtureDef();
            fixdf.isSensor = isSensor;
            PolygonShape shape = new PolygonShape();
            shape.set(vertices);
            fixdf.shape = shape;
            //filter
            fixdf.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
            fixdf.filter.maskBits = ColorMyWorldGame.PLAYER_BIT;
//            create
            body = world.createBody(bdf);
            body.createFixture(fixdf);
        }
//        rectangle object
        else if(RectangleMapObject.class.isAssignableFrom(object.getClass())){
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
            //filter
            fixdef.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
            fixdef.filter.maskBits = ColorMyWorldGame.PLAYER_BIT;
            //create
            body = world.createBody(bdf);
            body.createFixture(fixdef);
        }
        //Polygon object
        else if(PolygonMapObject.class.isAssignableFrom(object.getClass())){
            Polygon plygon= ((PolygonMapObject) object).getPolygon();
            Rectangle rect = plygon.getBoundingRectangle();
            BodyDef bdf = new BodyDef();
            bdf.position.set((rect.getX() + rect.getWidth() / 2 ) / ColorMyWorldGame.PPM,
                    (rect.getY() + rect.getHeight() / 2 ) / ColorMyWorldGame.PPM);
            bdf.type = BodyDef.BodyType.StaticBody;
            //fixtureDef
            FixtureDef fixdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            //vertices
            float[] vertices = plygon.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] / ColorMyWorldGame.PPM;
            }
            shape.set(vertices);
            fixdef.shape = shape;
            fixdef.isSensor = isSensor;
            //filter
            fixdef.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
            fixdef.filter.maskBits = ColorMyWorldGame.PLAYER_BIT;
            //create
            body = world.createBody(bdf);
            body.createFixture(fixdef);
        }
    }

    public Body getBody(){
        return body;
    }

    public void update(){
        if (isLive && setToDestroy){
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
