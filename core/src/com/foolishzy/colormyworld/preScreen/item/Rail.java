package com.foolishzy.colormyworld.preScreen.item;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.preScreen;

/**
 * Created by foolishzy on 2016/2/1.
 * <p>
 * funcction:
 * <p>
 * others:
 */
public class Rail {
    public Rail(World world, MapObject object){
        //polyline object
        if (PolylineMapObject.class.isAssignableFrom(object.getClass())) {
            Polyline plyline = ((PolylineMapObject) object).getPolyline();
            float[] vertices = plyline.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] / ColorMyWorldGame.PPM;
            }
            //BodyDef
            BodyDef bdf = new BodyDef();
            bdf.position.set(plyline.getX() / ColorMyWorldGame.PPM, plyline.getY() / ColorMyWorldGame.PPM);
            bdf.type = BodyDef.BodyType.StaticBody;
            //fixtureDef
            FixtureDef fixdf = new FixtureDef();
            fixdf.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
            fixdf.filter.maskBits = preScreen.BOX_BIT;
            PolygonShape shape = new PolygonShape();
            shape.set(vertices);
            fixdf.shape = shape;
            //create
            world.createBody(bdf).createFixture(fixdf);
        }
        //rectangle object
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
            fixdef.filter.categoryBits = ColorMyWorldGame.GROUND_BIT;
            fixdef.filter.maskBits = preScreen.BOX_BIT;
            //create
            world.createBody(bdf).createFixture(fixdef);
        }
    }
}
