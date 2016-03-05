package com.foolishzy.colormyworld.spark.Signal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;
import com.sun.media.jfxmediaimpl.MediaDisposer;

/**
 * Created by foolishzy on 2016/1/31.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Signal extends Actor implements MediaDisposer.Disposable{
    private Image borad, hint;
    private Vector2 position;
    private Vector2 ratio;
    private boolean hintIsVisable;
    private Stage stage;
    private float countTime = 0f;
    private float HINT_TIME = 2.5f;


    public Signal(MyScreen screen,MapObject positionObject,
                  TextureRegion boradRegion, TextureRegion hintRegion){
        /*
        * get position by positionObject
        * stage init in the screen,and has added listener
        * */

        //basic
        this.stage = screen.getMyStage();

        hintIsVisable = false;

        borad = new Image(boradRegion);
        hint = new Image(hintRegion);

        position = new Vector2(
                ((RectangleMapObject) positionObject).getRectangle().getX(),
                ((RectangleMapObject) positionObject).getRectangle().getY()
        );

        ratio = new Vector2(
                ((RectangleMapObject) positionObject).getRectangle().getWidth() /
                        boradRegion.getRegionWidth(),
                ((RectangleMapObject) positionObject).getRectangle().getHeight() /
                        boradRegion.getRegionHeight()
        );

        borad.setSize(
                boradRegion.getRegionWidth() * ratio.x,
                boradRegion.getRegionHeight() * ratio.y
        );
        hint.setSize(
                hintRegion.getRegionWidth() * ratio.x,
                hintRegion.getRegionHeight() * ratio.y
        );


        borad.setPosition(position.x, position.y);
        hint.setPosition(
                position.x + ((RectangleMapObject) positionObject).getRectangle().getWidth() / 2
                        - hint.getWidth() / 2,
                position.y + ((RectangleMapObject) positionObject).getRectangle().getHeight() / 2
        );

        hint.setVisible(hintIsVisable);
        borad.setVisible(true);

        //add listener
        borad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hintIsVisable = true;
                ColorMyWorldGame.PLAYER_TEMP_LOCK = true;
                Gdx.app.log("broad touched ", "player lock");
                Gdx.app.log("broad touched", " show hint ");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ColorMyWorldGame.PLAYER_TEMP_LOCK = false;
                Gdx.app.log("broad touch up ", " player unlock");
            }
        });

        //add actors
        stage.addActor(hint);
        stage.addActor(borad);

    }

    public void draw(float delta, Batch batch, float parentAlpha) {
        update(delta);
        stage.act();
        stage.draw();
    }

    private void update(float delta){
        if (countTime <= HINT_TIME && hintIsVisable){
            hint.setVisible(true);
            countTime += delta;
        }
        else{
            countTime = 0;
            hintIsVisable = false;
            hint.setVisible(hintIsVisable);
        }
    }

    @Override
    public void dispose() {

        hint.clear();
        borad.clear();
        stage.clear();
    }

}
