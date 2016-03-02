package com.foolishzy.colormyworld.preScreen.Sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.preScreen;

/**
 * Created by foolishzy on 2016/2/1.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class backGround implements Disposable{
    private Texture bgd;
    private Rectangle bound1;
    private Rectangle bound2;
    private preScreen screen;
    private Array<Rectangle> bound;

    public backGround(preScreen screen){
        //basic
        this.screen = screen;
        //texture
        bgd = new Texture("preScreenBackground2.jpg");
        //set bounds
        bound1 = new Rectangle(screen.getBackGround().getWidth() / ColorMyWorldGame.PPM, 0f,
                bgd.getWidth() / ColorMyWorldGame.PPM,  bgd.getHeight() / ColorMyWorldGame.PPM);
        bound2 = new Rectangle((screen.getBackGround().getWidth() + bgd.getWidth()) / ColorMyWorldGame.PPM,
                0f, bgd.getWidth() / ColorMyWorldGame.PPM, bgd.getHeight() / ColorMyWorldGame.PPM);
        bound = new Array<Rectangle>(2);
        bound.add(bound1);
        bound.add(bound2);
    }
    public void draw(Batch batch){
        update();
        for (Rectangle rectangle : bound) {
            batch.draw(bgd, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }
    }
    public void update(){
        for (Rectangle rectangle : bound) {
            if(rectangle.getX() + rectangle.getWidth() <
                    screen.getGameCam().position.x - ColorMyWorldGame.V_WIDTH / 2 /ColorMyWorldGame.PPM){
                rectangle.setPosition(rectangle.getX() + rectangle.getWidth() * 2, 0f);
            }
        }

    }

    @Override
    public void dispose() {
        bgd.dispose();
        bound.clear();
    }
}
