package com.foolishzy.colormyworld.playScreen1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;
import com.sun.media.jfxmediaimpl.MediaDisposer;

import javax.security.auth.login.FailedLoginException;

/**
 * Created by foolishzy on 2016/3/7.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class step extends Sprite implements MediaDisposer.Disposable {
    private Vector2 destination;
    private Vector2 size;
    private Vector2 originPosition;
    private MyScreen screen;

    private Body body;
    private TextureRegion step;
    private boolean isRising, isCatchDes, setToRise;
    private TextureRegion pillar;
    private Vector2 RISE_SPEED = new Vector2(0f, 1f);

    public step(MyScreen screen,RectangleMapObject object) {
        //resource
        step = new TextureRegion(new Texture("playScreen1/step.png"));
        pillar = new TextureRegion(new Texture("playScreen1/pillar.png"));
        this.screen = screen;

        isRising = false;
        isCatchDes = false;
        setToRise = false;

        //init
        define( object);

    }

    private void define( RectangleMapObject object){

        Rectangle initSizeRect =
                ((RectangleMapObject) screen.getMap().getLayers().get("stepInitSize").
                        getObjects().get("initSize")).getRectangle();
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        //size
        size = new Vector2(initSizeRect.getWidth() / screen.getPPM(),
                initSizeRect.getHeight() / screen.getPPM()
        );
        //destination
        destination = new Vector2(
                (rect.getX() + initSizeRect.getWidth() / 2) / screen.getPPM() ,
                (rect.getY() + rect.getHeight() - initSizeRect.getHeight() / 2) / screen.getPPM()
        );
        //originPosition
        originPosition = new Vector2(
                rect.getX()  /screen.getPPM(),
                rect.getY()  / screen.getPPM()
        );

        //init
                    //body
                BodyDef bdf = new BodyDef();
                bdf.type = BodyDef.BodyType.StaticBody;
                bdf.position.set(
                        (rect.getX() + initSizeRect.getWidth() / 2) / screen.getPPM(),
                        (rect.getY() + initSizeRect.getHeight() / 2) / screen.getPPM()
                );
                body = screen.getWorld().createBody(bdf);

                    //fixture
                FixtureDef fdf = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(
                        initSizeRect.getWidth() / screen.getPPM() / 2,
                        initSizeRect.getHeight() / screen.getPPM() / 2
                );
                fdf.shape = shape;
                fdf.filter.categoryBits = ColorMyWorldGame.STEP_BIT;
                fdf.filter.maskBits = ColorMyWorldGame.GROUND_BIT |
                        ColorMyWorldGame.PLAYER_BIT;

                body.createFixture(fdf);
        }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        //draw pillar
        if (isRising || isCatchDes) {
            batch.draw(pillar,
                    originPosition.x + size.x / 3,
                    originPosition.y,
                    size.x / 3,
                    body.getPosition().y - size.y / 2 - originPosition.y
                    );
        }
    }

    public void update(float dt){
        if (body.getPosition().y >= destination.y){
            isCatchDes = true;
            isRising = false;
            setToRise = false;
            body.setType(BodyDef.BodyType.StaticBody);
        }else if (setToRise){
            body.setLinearVelocity(RISE_SPEED);
            isRising = true;
        }

        //set region
        setPosition(body.getPosition().x  - size.x / 2,
                body.getPosition().y  - size.y / 2);
        setBounds(getX(), getY(), size.x, size.y);
        setRegion(step);
    }

    public void riseUp(){
        if (!isRising && !isCatchDes && !setToRise)
        setToRise = true;
        body.setType(BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void dispose() {
        screen.getWorld().destroyBody(body);
    }
}
