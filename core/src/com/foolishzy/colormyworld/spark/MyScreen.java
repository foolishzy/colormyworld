package com.foolishzy.colormyworld.spark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.foolishzy.colormyworld.ColorMyWorldGame;

/**
 * Created by foolishzy on 2016/1/31.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public abstract class MyScreen implements com.badlogic.gdx.Screen{
    protected ColorMyWorldGame game;
    protected Batch batch;
    protected FitViewport gamePort;
    protected OrthographicCamera gameCam;
    protected World world;
    protected TiledMap map;
    protected Box2DDebugRenderer b2drender;
    protected float PPM;
    protected com.badlogic.gdx.scenes.scene2d.Stage myStage;
    public MyScreen(ColorMyWorldGame game){
        this.PPM = ColorMyWorldGame.PPM;
        this.game = game;
        this.batch = game.batch;
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, ColorMyWorldGame.V_WIDTH / ColorMyWorldGame.PPM,
                ColorMyWorldGame.V_HEIGHT / ColorMyWorldGame.PPM);
        gamePort = new FitViewport(ColorMyWorldGame.V_WIDTH / ColorMyWorldGame.PPM,
                ColorMyWorldGame.V_HEIGHT / ColorMyWorldGame.PPM, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        myStage = new com.badlogic.gdx.scenes.scene2d.Stage(gamePort);
        Gdx.input.setInputProcessor(myStage);

        world = new World(new Vector2(0f, -10f), true);
        b2drender = new Box2DDebugRenderer();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public float getPPM() {
        return PPM;
    }

    public TiledMap getMap(){
        return map;
    }

    public World  getWorld(){
        return world;
    }

    public Stage getMyStage(){return myStage;}
}
