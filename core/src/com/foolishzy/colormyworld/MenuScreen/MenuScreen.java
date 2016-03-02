package com.foolishzy.colormyworld.MenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.playScreen.bridgeScreen;
import com.foolishzy.colormyworld.spark.MyScreen;

/**
 * Created by foolishzy on 2016/2/3.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class MenuScreen extends MyScreen {
    private Texture backGround;
    private Stage stage;
    private ImageButton playButn;
    private Image Title;
    private Vector2 Scale;
    private boolean setPlayScreen;

    public MenuScreen(ColorMyWorldGame game) {
        super(game);
        setPlayScreen = false;
        //init
        backGround = new Texture("MenuScreen/menuback.jpg");
        Scale = new Vector2(gamePort.getWorldWidth() / backGround.getWidth(),
                gamePort.getWorldHeight() / backGround.getHeight());
        //stage
        stage  = new Stage(gamePort);
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.sequence(Actions.scaleTo(1.02f,1.02f, 1f),Actions.scaleTo(1f, 1f, 1f)));

        //title
        Texture title = new Texture("MenuScreen/Title.png");
        Title = new Image(new TextureRegion(title));
        Title.setSize(title.getWidth() * Scale.x, title.getHeight() * Scale.y);
        Title.setPosition(0, gamePort.getWorldHeight() - Title.getHeight());
        //scale action
        ScaleToAction scaleBig = Actions.scaleTo(1.04f, 1.04f, 2.5f);
        Title.addAction(scaleBig);
        stage.addActor(Title);

        //play button
        TextureRegionDrawable buttonUp = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen/playButtonUp.png")));
        TextureRegionDrawable buttonDown = new TextureRegionDrawable(new TextureRegion(new Texture("MenuScreen/playButtonDown.png")));
        playButn  = new ImageButton(buttonUp, buttonDown);
        playButn.setSize(playButn.getWidth() * Scale.x, playButn.getHeight() * Scale.y);
        playButn.setPosition(((float) (gamePort.getWorldWidth() - playButn.getWidth() - 0.5)), 0.5f);
        playButn.addAction(Actions.scaleTo(0.97f, 0.97f, 2.5f));
        stage.addActor(playButn);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        update(delta);
        //clear color red
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        batch.draw(backGround, 0, 0, gamePort.getWorldWidth(), gamePort.getWorldHeight());
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void update(float dt){
        if (playButn.isPressed()){
            playButn.setDisabled(true);
            stage.addAction(Actions.sequence(Actions.fadeOut(0.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            setPlayScreen = true;
                        }
                    })));
        }
        if(setPlayScreen){
            game.setScreen(new bridgeScreen(((ColorMyWorldGame) game)));
            dispose();
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
        backGround.dispose();
        Title.clear();
        playButn.clear();
        stage.dispose();
    }
    //
}
