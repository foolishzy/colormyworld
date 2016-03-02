package com.foolishzy.colormyworld.preScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.preScreen.Listener.Listener;
import com.foolishzy.colormyworld.preScreen.Sprite.Box;
import com.foolishzy.colormyworld.preScreen.Sprite.slideFrame;
import com.foolishzy.colormyworld.preScreen.item.Rail;
import com.foolishzy.colormyworld.preScreen.item.dieArea;
import com.foolishzy.colormyworld.preScreen.Sprite.backGround;
import com.foolishzy.colormyworld.spark.Spark;


/**
 * Created by foolishzy on 2016/1/31.
 * <p>
 * funcction:
 * <p>
 * others:
 */
public class preScreen implements com.badlogic.gdx.Screen {
    //basic
    private ColorMyWorldGame game;
    private Batch batch;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private float Timer;
    private int SELECT_WAITTING_TIME = 7;
    //tiledMap
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private TmxMapLoader tmxLoader;
    //background
    private Texture backGround ;
    private backGround bgd;
    //box2d
    private World world;
    private Box2DDebugRenderer b2drender;
    private static Vector2 GRAVITY = new Vector2(0f, -10f);
    //BOXBIT
    public static short BOX_BIT = 16;
    private Box box;
    //cam velocity
    private Vector2 camVelocity;


    private slideFrame slide1;
    private Spark spark;

    public preScreen(ColorMyWorldGame game){
        this.batch = game.batch;
        this.game = game;
        //basic
        Timer = 0;
        camVelocity = new Vector2(0.001f, 0f);
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, ColorMyWorldGame.V_WIDTH / ColorMyWorldGame.PPM,
                ColorMyWorldGame.V_HEIGHT / ColorMyWorldGame.PPM);
        gamePort = new FitViewport(ColorMyWorldGame.V_WIDTH / ColorMyWorldGame.PPM,
                ColorMyWorldGame.V_HEIGHT / ColorMyWorldGame.PPM, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //tiledMap
        tmxLoader = new TmxMapLoader();
        map = tmxLoader.load("preScreenMap/preScreen.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f/ ColorMyWorldGame.PPM);
        //box2d
        world = new World(GRAVITY, true);
        b2drender = new Box2DDebugRenderer();
        world.setContactListener(new Listener());
        //born and die position
        initborndie();
        //rail where the box slide
        initRail();
        //box
        box = new Box(this);
        //the frist background
        backGround = new Texture("preScreenMap/preScreenBackground1.jpg");
        //next background
        bgd = new backGround(this);

        spark = new Spark(0,0,100,100);
        slide1 = new slideFrame(this);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        //red
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        //background
        batch.draw(backGround, 0, 0,
                backGround.getWidth() / ColorMyWorldGame.PPM,
                backGround.getHeight() / ColorMyWorldGame.PPM);
        bgd.draw(batch);
        //box
        box.draw(batch);
        batch.end();
        slide1.draw(batch, delta);

//        map
//        renderer.setView(gameCam);
//        renderer.render();

        //b2drender
        b2drender.render(world, gameCam.combined);

        spark.draw(delta);



    }

    private void update(float dt){
        //world step
        world.step(1f / 50f, 6, 6);
        //update box
        if(box.isDestoried()){
            box = new Box(this);
        }
        //play button pressed
        if(slide1.getPlayButnPressed()){
            //update cam position
            camVelocity.add(dt / 10, 0).scl(0.98f);
            gameCam.position.x += camVelocity.x;
            gameCam.update();
            //slide frame begin action
            /*
            Timer > slide1.getImg1ActionTime() +
                slide1.getImg2ActionTime() + SELECT_WAITTING_TIME
                timer stop count
             */
            if(slide1.getIsAction() && Timer < slide1.getImg1ActionTime() +
                    slide1.getImg2ActionTime() + SELECT_WAITTING_TIME){
                //timer count
                Timer += dt;
            }
        }
        //check to set menu screen
        if(slide1.getSkipPressed() || Timer > slide1.getImg1ActionTime() +
                slide1.getImg2ActionTime() + SELECT_WAITTING_TIME){
            slide1.fadeOut(game);
        }
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
        bgd.dispose();
        map.dispose();
        renderer.dispose();
        backGround.dispose();
        box.dispose();
        slide1.dispose();
        spark.dispose();
        world.dispose();
    }

    private void initRail(){
        for (MapObject object : map.getLayers().get(2).getObjects()){
            new Rail(world, object);
        }
    }

    private void initborndie(){
        for (MapObject object : map.getLayers().get(3).getObjects()){
            if(object.getProperties().containsKey("born")){
                // bornArea  never  used
//                new bornArea(world, object);
            }else {
                new dieArea(world, object);
            }
        }
    }
    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public OrthographicCamera getGameCam(){return gameCam;}

    public Texture getBackGround(){
        return backGround;
    }
}
