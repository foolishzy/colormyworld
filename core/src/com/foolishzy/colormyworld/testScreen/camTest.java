package com.foolishzy.colormyworld.testScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.foolishzy.colormyworld.ColorMyWorldGame;
import com.foolishzy.colormyworld.spark.MyScreen;

import javax.xml.soap.Text;

/**
 * Created by foolishzy on 2016/3/8.
 * <p/>
 * funcction:
 * <p/>
 * others:
 */
public class camTest extends MyScreen {
    private Texture bg;
    private OrthographicCamera testCam;
    public camTest(ColorMyWorldGame game) {
        super(game);
        bg = new Texture("MenuScreen/menuback.jpg");
        testCam = new OrthographicCamera(gamePort.getWorldWidth() / 2,
                gamePort.getWorldHeight() / 2);
        testCam.position.set(gameCam.position.x,
                gameCam.position.y,
                gameCam.position.z
        );
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(bg, testCam.position.x - testCam.viewportWidth / 2,
                testCam.position.x - testCam.viewportHeight / 2);
        batch.end();
        batch.setProjectionMatrix(testCam.combined);
        batch.begin();
        batch.draw(bg, testCam.position.x - testCam.viewportWidth / 2,
                testCam.position.x - testCam.viewportHeight / 2);
        batch.end();
    }
}
