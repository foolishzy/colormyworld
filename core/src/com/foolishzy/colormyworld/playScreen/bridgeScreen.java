package com.foolishzy.colormyworld.playScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.item.staticItem;
import com.foolishzy.colormyworld.spark.MyScreen;
import com.foolishzy.colormyworld.spark.Spark;

import javax.swing.text.html.parser.DTD;


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

    //spark test
    private Spark test;

    public bridgeScreen(ColorMyWorldGame game) {
        super(game);
    //test
        test = new Spark(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 100 / PPM, 100 / PPM);

        //spark
        sparkList = new Array<Spark>();
        //background
        backGround = new Texture("bridgeScreen/backGround.png");
        //map
        map = new TmxMapLoader().load("bridgeScreen/bridge.tmx");
        //init box2d&player
        initBox2dandPlayer();

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
    }

    private void handleinput(float dt){
        //bridge
        if(!bridge.isRotate() && Gdx.input.isKeyPressed(Input.Keys.Q)){
            bridge.rotateBridge();
            Gdx.app.log("Q pressed", "rotate bridge");
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
        super.dispose();
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
            sparkList.add(new Spark(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
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

}
