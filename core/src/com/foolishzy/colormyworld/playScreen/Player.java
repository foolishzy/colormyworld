package com.foolishzy.colormyworld.playScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;

/**
 * Created by foolishzy on 2016/2/3.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Player extends Sprite {
    public Body body;
    private World world;
    private float PPM;
    private Vector2 jumpImpluse = new Vector2(0, 0.3f);
    private Vector2 runImpluse = new Vector2(0.3f, 0);



    public Player(MyScreen screen, World world, RectangleMapObject born){
        this.world = world;
        this.PPM = screen.getPPM();
        definePlayer(born);
    }

    private void definePlayer(RectangleMapObject born){
        Rectangle rect = born.getRectangle();
        //BodyDef
        BodyDef bdf = new BodyDef();
        bdf.position.set((rect.getX() + rect.getWidth() / 2 ) / PPM,
                (rect.getY() + rect.getHeight() / 2) / PPM);
        bdf.type = BodyDef.BodyType.DynamicBody;
        //fixtureDef
        FixtureDef fixdef = new FixtureDef();
        fixdef.filter.categoryBits = ColorMyWorldGame.PLAYER_BIT;
        fixdef.filter.maskBits = ColorMyWorldGame.GROUND_BIT ;
        //shape
        CircleShape shape = new CircleShape();
        shape.setRadius(6f / PPM);
        fixdef.shape = shape;
        //create
        body = world.createBody(bdf);
        body.createFixture(fixdef).setUserData(this);
    }

    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            System.out.println("up");
            System.out.println(body.getWorldCenter());
            body.applyLinearImpulse(jumpImpluse, body.getWorldCenter(), true);
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            body.applyLinearImpulse(runImpluse, body.getWorldCenter(), true);
            System.out.println("right");
        }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            System.out.println("left");
            body.applyLinearImpulse(new Vector2( - runImpluse.x, runImpluse.y), body.getWorldCenter(), true);
        }

    }

}
