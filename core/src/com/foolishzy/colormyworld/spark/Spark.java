package com.foolishzy.colormyworld.spark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.foolishzy.colormyworld.ColorMyWorldGame;

/**
 * Created by foolishzy on 2016/1/30.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class Spark extends Actor   implements Disposable {
    private Array<Image> imgList;
    private static final int TOTAL_NUM = 10;
    private Image checkLayer;
    private FitViewport viewport;
    private OrthographicCamera cam;
    private Stage stage;
    private boolean isTouched;
    private Batch batch;

    public Spark(float x, float y, float width, float height){
        isTouched = false;

        /*
        because the img has fadin action, during the these time ,
        the alpha of the batch will change,and in the draw method
        the alpha is changing ,too. if we use the same batch,
        the screen will blink
                         */
        batch = new SpriteBatch();

        cam = new OrthographicCamera();
        viewport = new FitViewport(ColorMyWorldGame.V_WIDTH,
                ColorMyWorldGame.V_HEIGHT, cam);
        stage = new Stage(viewport);

        //checkLayer transparent
        checkLayer = new Image(new Texture(new Pixmap((int) width,
                ((int) height), Pixmap.Format.Alpha)));
        checkLayer.setSize(width, height);
        checkLayer.setPosition(x, y);

        //check if touched or not
        Gdx.input.setInputProcessor(stage);
        checkLayer.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isTouched = true;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ColorMyWorldGame.PLAYER_TEMP_LOCK = true;
                return;
            }
        });
        stage.addActor(checkLayer);

        // init imgList
        imgList = new Array<Image>(TOTAL_NUM);


        for(int i = 0; i < TOTAL_NUM;  i++){
            Image img = new Image(new Texture("bridgeScreen/spark.png"));
            //resize
            float scale = MathUtils.random(0.2f, 0.5f);
            img.setSize(scale * width, scale * height);
            //reposition
            img.setPosition(x + MathUtils.random(0f, 1f)*(width - img.getWidth()),
                    y + MathUtils.random(0f, 1f)*(height - img.getHeight()));
            //set action
            SequenceAction sequence = Actions.sequence(
                    Actions.fadeIn(MathUtils.random(1f, 2f)),
                    Actions.fadeOut(MathUtils.random(1f, 2f)));
            img.addAction(Actions.forever(sequence));
            imgList.add(img);
        }
    }
//  to check if these spark overlap and then recreate
    // but not works well
//        for (int i = 0; i < TOTAL_NUM; i++){
//            imgList.add(createSpark(x, y ,width, height));
//            for (int j = 0; j < i; j++){
//                Image imagei = imgList.get(i);
//                Image imagej = imgList.get(j);
//                while ((imagei.getX() > imagej.getX() -imagej.getWidth() / 5 &&
//                        imagei.getRight() < imagej.getRight() + imagei.getWidth() / 5)
//                    ||(imagei.getY() > imagej.getY() -imagej.getHeight() / 5 &&
//                            imagei.getTop() < imagej.getTop() + imagei.getHeight() / 5))
//                    {imgList.removeIndex(i);
//                        imgList.add(createSpark(x, y ,width, height));
//                    }
//                }
//            }
//        }

    // random alpha
    public void draw( float dt){
        batch.setProjectionMatrix(cam.combined);
        if(!isTouched){
            stage.act();
            batch.begin();
            checkLayer.draw(batch, 0f);
            for (Image image : imgList) {
                //draw
                image.draw(batch, MathUtils.random(0.3f, 1f));
            }
            batch.end();
        }else{
            dispose();
        }
    }

    @Override
    public void dispose() {
        for (Image image : imgList) {
            image.clear();
        }
        imgList.clear();
        checkLayer.clear();
        stage.clear();
    }
    }

