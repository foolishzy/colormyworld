package com.foolishzy.colormyworld.playScreen1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.playScreen.Player;
import com.foolishzy.colormyworld.playScreen2.playScreen2;
import com.foolishzy.colormyworld.preScreen.item.staticItem;
import com.foolishzy.colormyworld.spark.MyScreen;
import com.foolishzy.colormyworld.spark.Signal.Signal;
import com.foolishzy.colormyworld.spark.Spark;

/**
 * Created by foolishzy on 2016/3/5.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class playScreen1 extends MyScreen {
    private Texture background;
    private Spark switchOfStep;
    private Signal signal;
    private TextureRegion hintRegion, boradRegion;
    private Player player;
    private Array<step> steps;
    public playScreen1(ColorMyWorldGame game) {
        super(game);
        background = new Texture("playScreen1/background1.png");
        //hintRegion needs to be replaced
        hintRegion = new TextureRegion(new Texture("bridgeScreen/signal1.png"));
        boradRegion = new TextureRegion(new Texture("bridgeScreen/signal.png"));
        steps = new Array<step>(3);
        //init
        define();

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        update(delta);
        //clear
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //projection
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        //background
        batch.draw(background, 0, 0, gamePort.getWorldWidth(), gamePort.getWorldHeight());
        //steps
        for (step step : steps) {
            step.draw(batch);
        }
        batch.end();
        //signal
        signal.draw(delta, batch, 1f);
        //spark
        switchOfStep.draw(delta);

        //b2d
        b2drender.render(world, gameCam.combined);
    }

    private void update(float delta){
        //step world
        world.step(1 / 60f, 6, 4);

        //player update
        player.update(delta);

        //step rise up
        if (switchOfStep.isSwitched()) {
            for (step step : steps) {
                step.riseUp();
            }
        }
        //steps update
        for (step step : steps) {
            step.update(delta);
        }
        //set next screen
        if (player.catchRight())
            over();

    }

    private void define(){
        map = new TmxMapLoader().load("playScreen1/playScreen1.tmx");
        //steps
        for (MapObject object : map.getLayers().get("step").getObjects()) {
            steps.add( new step(this, ((RectangleMapObject) object)));
        }
        //player
        player = new Player(
                this,
                world,
                ((RectangleMapObject) map.getLayers().get(1).getObjects().get("bron"))
        );

        //ground
        for (MapObject object : map.getLayers().get(0).getObjects()) {
            new staticItem(world, object, false);
        }

        //signal
        for (MapObject object : map.getLayers().get(2).getObjects()) {
            signal = new Signal(this, object, boradRegion, hintRegion);
        }
        //spark
        for (MapObject object : map.getLayers().get("sparks").getObjects()) {
            //object must be rectangleMapObject
            if (!RectangleMapObject.class.isAssignableFrom(object.getClass())) {
                Gdx.app.log("init spark mistake " ," must be rectangleMapObject");
            }
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (object.getProperties().containsKey("switch")) {
                switchOfStep = new Spark(
                        rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight(),
                        this
                );
            }
        }


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
        switchOfStep.dispose();
        background.dispose();
        signal.dispose();
        for (step step : steps) {
            step.dispose();
        }
        steps.clear();
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
        Gdx.app.log("playScreen1 is over,","loading next screen...");
        dispose();
        game.setScreen(new playScreen2(((ColorMyWorldGame) game)));
    }
}
