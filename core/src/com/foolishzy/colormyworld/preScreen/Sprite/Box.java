package com.foolishzy.colormyworld.preScreen.Sprite;

import com.badlogic.gdx.graphics.Pixmap;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.preScreen;
import com.sun.media.jfxmediaimpl.MediaDisposer;


/**
 * Created by foolishzy on 2016/2/1.
 * <p>
 * funcction:
 * <p>
 * others:
 */
public class Box extends Sprite implements Disposable{
    private World world;
    private TiledMap map;
    private Body body;
    private Vector2 impulse;
    private TextureRegion boxImg;
    private Vector2 bound;
    private boolean setToDestory;
    private boolean isDestoried;
    private boolean isOutofScreen;
    private int layerIndex = 4;
    private float stateTimer;
    private preScreen screen;
    public Box(preScreen screen){

        //basic
        this.screen = screen;
        this.map = screen.getMap();
        this.world = screen.getWorld();
        //b2d
        initbox2d(layerIndex);
        impulse = new Vector2(0.03f, 0f);

        setToDestory = false;
        isDestoried = false;
        isOutofScreen = false;

        boxImg = new TextureRegion(new Texture("box.png"));
        bound = new Vector2(boxImg.getRegionWidth() / 2f, boxImg.getRegionHeight() / 2f);
        setPosition(body.getPosition().x - bound.x / 2 / ColorMyWorldGame.PPM,
                body.getPosition().y - bound.y / 2 / ColorMyWorldGame.PPM);
        setBounds(getX(), getY(), bound.x / ColorMyWorldGame.PPM, bound.y / ColorMyWorldGame.PPM);
        setRegion(boxImg);
    }

    @Override
    public void draw(Batch batch) {
        update();
        super.draw(batch);
    }

    private void update(){
        if(  !isDestoried && !setToDestory){
            setPosition(body.getPosition().x - bound.x / 2 / ColorMyWorldGame.PPM,
                    body.getPosition().y - bound.y / 2 / ColorMyWorldGame.PPM);
            setBounds(getX(), getY(), bound.x / ColorMyWorldGame.PPM, bound.y / ColorMyWorldGame.PPM);
            if(body.getLinearVelocity().x < 1 ){
                body.applyLinearImpulse(impulse, body.getWorldCenter(), false);
            }
        }else if (  setToDestory && !isDestoried){
            isDestoried = true;
            //set a new transparent region
            setRegion(new TextureRegion(new Texture(new Pixmap(((int) bound.x), ((int) bound.y), Pixmap.Format.Alpha))));
            world.destroyBody(body);
        }else if( setToDestory && isDestoried){
            isDestoried = true;
        }
        if(screen.getGameCam().position.x > ColorMyWorldGame.V_WIDTH / ColorMyWorldGame.PPM){
            isOutofScreen = true;
            body.setActive(false);
        }
    }

    private void initbox2d(int layerIndex){
        for (MapObject object : map.getLayers().get(layerIndex).getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //BodyDef
            BodyDef bdf = new BodyDef();
            bdf.position.set((rect.getX() + rect.getWidth() / 2 ) / ColorMyWorldGame.PPM,
                    (rect.getY() + rect.getHeight()/ 2) /ColorMyWorldGame.PPM);
            bdf.type = BodyDef.BodyType.DynamicBody;
            //fixtureDef
            FixtureDef fixdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / ColorMyWorldGame.PPM,
                    rect.getHeight() / 2 / ColorMyWorldGame.PPM);
            fixdef.shape =  shape;
            fixdef.density = 3f;
            fixdef.filter.categoryBits = preScreen.BOX_BIT;
            fixdef.filter.maskBits = ColorMyWorldGame.GROUND_BIT |
                                    ColorMyWorldGame.DESTINATION_BIT;
            //create
            body = world.createBody(bdf);
            body.createFixture(fixdef).setUserData(this);


        }

    }

    public void getDestination(){
        setToDestory = true;
    }

    public boolean isDestoried(){
        return isDestoried;
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
