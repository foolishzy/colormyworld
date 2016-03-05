package com.foolishzy.colormyworld.playScreen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.playScreen1.playScreen1;
import com.foolishzy.colormyworld.preScreen.item.staticItem;
import com.foolishzy.colormyworld.spark.MyScreen;
import com.foolishzy.colormyworld.spark.Signal.Signal;
import com.foolishzy.colormyworld.spark.Spark;


/**
 * Created by foolishzy on 2016/2/3.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class bridgeScreen extends MyScreen {
    private Texture backGround;
    private Player Player;
    private Array<Spark> sparkList;
    private Bridge bridge;
    private Spark switchOfbridge;

    //spark test
    private Spark test;

    private Array<Signal> signals;
    private TextureRegion broadRegion;
    private Array<TextureRegion> hintRegions;
    private boolean isOver = false;

    public bridgeScreen(ColorMyWorldGame game) {
        super(game);
        //test
        test = new Spark(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 100 / PPM, 100 / PPM, this);
        //spark
        sparkList = new Array<Spark>();
        //background
        backGround = new Texture("bridgeScreen/backGround.png");
        //map
        map = new TmxMapLoader().load("bridgeScreen/bridge.tmx");
        //init box2d&player
        initBox2dandPlayer();
        //init signals
        initSignal();

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {

        //update
        update(delta);

        //red clear
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        //background
        batch.draw(backGround, 0, 0, gamePort.getWorldWidth(), gamePort.getWorldHeight());
        //bridge draw
        bridge.draw(batch);
        batch.end();
        //spark
        for (Spark spark : sparkList) {
            spark.draw(delta);
        }

        //
        test.draw(delta);

        //signal draw
        for (Signal signal : signals){
            signal.draw(delta, batch, 1f);
        }

        //box2d
        b2drender.render(world, gameCam.combined);
    }

    private void update(float dt){
        //handle input
        handleinput(dt);
        //step world
        world.step(1 / 50f, 6, 6);
        //player update
        Player.update(dt);
        //bridge update
        bridge.update();
        //check isCatch destination
        if (!isOver && Player.body.getWorldCenter().x > gamePort.getWorldWidth()) {
            isOver = true;
            this.over();
            Gdx.app.log("player out screen"," start new screen");
        }
    }

    private void handleinput(float dt){
        //bridge
        if (!bridge.isRotate() && switchOfbridge.isSwitched()){
            bridge.rotateBridge();
            Gdx.app.log("switch touched ", "rotate bridge");
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
        backGround.dispose();
        for (Spark spark : sparkList) {
            spark.dispose();
        }
        sparkList.clear();
        bridge.dispose();
        for (Signal signal : signals) {
            signal.dispose();
        }
    }

    private void initBox2dandPlayer(){
        //ground
        for (MapObject object : map.getLayers().get(0).getObjects()) {
            new staticItem(world, object, false);
        }

        //spark 1
        for(MapObject object : map.getLayers().get(1).getObjects()){
            new staticItem(world, object, true);
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            sparkList.add(new Spark(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), this));

            //bridge switch
            if (object.getProperties().containsKey("switch")) {
                switchOfbridge = new Spark(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), this);
            }

        }
        //signal 2
        for(MapObject object : map.getLayers().get(2).getObjects()){
            new staticItem(world, object , true);
        }
        //born&destination 3
        for(MapObject object : map.getLayers().get(3).getObjects()){
            new staticItem(world, object, true);
        }
        //player
        MapObject bornPlace = map.getLayers().get(3).getObjects().get("born");
        Player = new Player(this, world, ((RectangleMapObject) bornPlace));
        //bridge
        bridge = new Bridge(this);
    }

    private void initSignal(){
        broadRegion = new TextureRegion(new Texture("bridgeScreen/signal.png"));
        hintRegions = new Array<TextureRegion>();
        hintRegions.add(new TextureRegion(new Texture("bridgeScreen/signal1.png")));
        hintRegions.add(new TextureRegion(new Texture("bridgeScreen/signal2.png")));
        hintRegions.add(new TextureRegion(new Texture("bridgeScreen/signal3.png")));
        signals = new Array<Signal>();

        for (MapObject object : map.getLayers().get(2).getObjects()) {
            if (object.getClass().isAssignableFrom(RectangleMapObject.class)) {
                if (object.getProperties().containsKey("1")) {
                    signals.add(new Signal(this, object, broadRegion, hintRegions.get(0)));
                }else if (object.getProperties().containsKey("2")) {
                    signals.add(new Signal(this, object, broadRegion, hintRegions.get(1)));
                }else signals.add(new Signal(this, object, broadRegion , hintRegions.get(2)));
            }else{
                Gdx.app.log(
                        "mistake in bridge screen",
                        "init signal, positionObject isn't rectanglemapobject"
                );
            }
        }
    }

    @Override
    public void over() {
        game.setScreen(new playScreen1(((ColorMyWorldGame) game)));
        dispose();
        Gdx.app.log("bridgeScreen is over", "dispose resource");
    }
}
