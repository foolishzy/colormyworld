package com.foolishzy.colormyworld.preScreen.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.MenuScreen.MenuScreen;
import com.foolishzy.colormyworld.preScreen.preScreen;


/**
 * Created by foolishzy on 2016/2/1.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class slideFrame extends Actor implements Disposable{
    private preScreen screen;
    private Stage stage;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Image img1;
    private Image img2;
    private TextureRegion region1;
    private TextureRegion region2;
    //count time to play these actions
    private static float countTimer = 4;
    private static float IMG1_ACTION_TIME = 4;
    private static float IMG2_ACTION_TIME = 3;
    private ImageButton playButn;
    private ImageButton skipButn;
    private boolean isAction;


    public slideFrame(preScreen screen){
        this.screen = screen;
        isAction = false;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(ColorMyWorldGame.V_WIDTH, ColorMyWorldGame.V_HEIGHT , gameCam);
        stage = new Stage(gamePort);
        //play button
        TextureRegionDrawable buttonUp = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen/playButtonUp.png")));
        TextureRegionDrawable buttonDown = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen/playButtonDown.png")));
        playButn = new ImageButton(buttonUp, buttonDown);
        playButn.setPosition(gamePort.getWorldWidth() / 2 - playButn.getWidth() / 2,
                gamePort.getWorldHeight() / 2 - playButn.getHeight() / 2);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(playButn);
        // slide frame
        region1 = new TextureRegion(new Texture("preScreenfristaction.png"));
        region2 = new TextureRegion(new Texture("preScreenSeconedAction.png"));

        this.img2 = new Image(region2);
        this.img1 = new Image(region1);
        //图片中的角度是74度 tan(74)=3.48
        //resize
        img1.setSize(region1.getRegionWidth() * ColorMyWorldGame.V_HEIGHT / region1.getRegionHeight(),
                ColorMyWorldGame.V_HEIGHT);
        img2.setSize(region2.getRegionWidth()*ColorMyWorldGame.V_HEIGHT / region2.getRegionHeight(),
                ColorMyWorldGame.V_HEIGHT);

        //set origin position
        img1.setPosition(((float) (gamePort.getWorldWidth() / 2 + gamePort.getWorldWidth() / 2 / 3.48 -
                        img1.getWidth() - gamePort.getWorldHeight() / 3.48)), - img1.getHeight());
        img2.setPosition(((float) (gamePort.getWorldWidth() / 2 + gamePort.getWorldHeight() / 2 / 3.48)),
                gamePort.getWorldHeight());
        //img1 actions
        MoveToAction moveTo = Actions.moveTo(((float) (gamePort.getWorldWidth() / 2 -
                img1.getWidth() +
                img1.getHeight() / 2 /3.48)),
                0, IMG1_ACTION_TIME );
        AlphaAction fadein = Actions.fadeIn(IMG1_ACTION_TIME);
        img1.addAction(Actions.parallel(fadein, moveTo));
        stage.addActor(img1);
        //img2 actions
        moveTo = Actions.moveTo(((float) (gamePort.getWorldWidth() / 2 - gamePort.getWorldHeight() / 2 / 3.48)), 0, IMG2_ACTION_TIME );
        fadein = Actions.fadeIn(IMG1_ACTION_TIME);

        //sequence action play action2 when action1 finished
        img2.addAction(Actions.sequence(fadein, moveTo));
        stage.addActor(img2);

        //skip button
        buttonUp = new TextureRegionDrawable(new TextureRegion(new Texture("preScreenMap/skipUp.png")));
        buttonDown = new TextureRegionDrawable(new TextureRegion(new Texture("preScreenMap/skipDown.png")));
        skipButn = new ImageButton(buttonUp, buttonDown);
        skipButn.setSize(skipButn.getWidth() * 0.8f, skipButn.getHeight() * 0.8f);
        skipButn.setPosition(gamePort.getWorldWidth() - skipButn.getWidth(), 0f);
    }

    private void update(float dt){
        if (playButn.isChecked()) {
            playButn.setDisabled(true);
            playButn.addAction(Actions.fadeOut(0.4f));
            playButn.act(dt);
        }
        if(skipButn.isChecked()){
            skipButn.setDisabled(true);
            skipButn.addAction(Actions.fadeOut(0.4f));
        }
    }

    public void draw(Batch batch,float dt) {
        update(dt);
        if(countTimer < 0){
            createSkipButn();
            stage.act();
            isAction = true;
        }else if(playButn.isChecked()){
            countTimer -= dt;
        }
        stage.draw();
    }

    public void fadeOut(final ColorMyWorldGame game){
        Action endAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(((ColorMyWorldGame) game)));
            }
        });
        //stage fade out and then set menuScreen
        stage.addAction(new SequenceAction(Actions.fadeOut(0.5f), endAction));
    }


    @Override
    public void dispose() {
        img1.clear();
        img2.clear();
        skipButn.clear();
        stage.clear();
    }
    public boolean getPlayButnPressed(){
        return playButn.isChecked();
    }

    public boolean getSkipPressed(){
        return skipButn.isChecked();
    }

    private void createSkipButn(){
        stage.addActor(skipButn);
    }

    public float getImg1ActionTime() {
        return IMG1_ACTION_TIME;
    }

    public float getImg2ActionTime() {
        return IMG2_ACTION_TIME;
    }

    public boolean getIsAction(){
        return isAction;
    }
}
