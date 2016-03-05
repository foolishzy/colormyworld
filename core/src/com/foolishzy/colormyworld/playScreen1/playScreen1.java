package com.foolishzy.colormyworld.playScreen1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;

/**
 * Created by foolishzy on 2016/3/5.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class playScreen1 extends MyScreen {
    public playScreen1(ColorMyWorldGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public float getPPM() {
        return super.getPPM();
    }

    @Override
    public TiledMap getMap() {
        return super.getMap();
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public Stage getMyStage() {
        return super.getMyStage();
    }

    @Override
    public OrthographicCamera getStageCam() {
        return super.getStageCam();
    }

    @Override
    public void over() {
        super.over();
    }
}
