package com.foolishzy.colormyworld.preScreen.item;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
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
 */
public class bornArea {
    public bornArea(World world, MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        BodyDef bdf = new BodyDef();
        bdf.position.set((rect.getX() + rect.getWidth() / 2) / ColorMyWorldGame.PPM,
                (rect.getY() + rect.getHeight() / 2) / ColorMyWorldGame.PPM);
        bdf.type = BodyDef.BodyType.StaticBody;
        FixtureDef fixdf = new FixtureDef();
        PolygonShape shpe = new PolygonShape();
        shpe.setAsBox(rect.getWidth() / 2 / ColorMyWorldGame.PPM,
                rect.getHeight() / 2 / ColorMyWorldGame.PPM);
        fixdf.shape = shpe;
        fixdf.isSensor = true;
        fixdf.filter.categoryBits = ColorMyWorldGame.BORN_BIT;
        fixdf.filter.maskBits = ColorMyWorldGame.PLAYER_BIT;
        world.createBody(bdf).createFixture(fixdf);
    }
}
