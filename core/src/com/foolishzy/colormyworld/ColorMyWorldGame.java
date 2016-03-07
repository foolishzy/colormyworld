package com.foolishzy.colormyworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.foolishzy.colormyworld.playScreen.bridgeScreen;
import com.foolishzy.colormyworld.preScreen.preScreen;
import com.foolishzy.colormyworld.testScreen.camTest;
import com.foolishzy.colormyworld.testScreen.distanceJointTestScreen;
import com.foolishzy.colormyworld.testScreen.polyLineTest;
import com.foolishzy.colormyworld.testScreen.testScreen;

import java.util.BitSet;


public class ColorMyWorldGame extends Game{
	public static String TITLE = "Color MY World";
	public static  int V_WIDTH = 800;
	public static  int V_HEIGHT = 400;
	public static final int PPM = 100;
	public static  boolean PLAYER_TEMP_LOCK = false;
	public SpriteBatch batch;
	public static  AssetManager ASSET_MANGER;
	public static final short BORN_BIT = 2;
	public static final short DESTINATION_BIT = 4;
	public static final short GROUND_BIT = 8;
	public static final short PLAYER_BIT = 16;
	public static final short NAIL_BIT =32;
	public static final short STEP_BIT = 64;



	@Override
	public void create() {
		batch = new SpriteBatch();
		//init assert manger
		ASSET_MANGER = new AssetManager();
		ASSET_MANGER.load("BGM.mp3", Music.class);
		ASSET_MANGER.finishLoading();
		// play bgm
		Music bgm = ASSET_MANGER.get("BGM.mp3", Music.class);
		bgm.setLooping(true);
		bgm.play();

		//set screen
//		setScreen(new bridgeScreen(this));

		setScreen(new camTest(this));

	}


	public ColorMyWorldGame() {
		super();
	}

	@Override
	public void dispose() {
		super.dispose();
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
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	@Override
	public Screen getScreen() {
		return super.getScreen();
	}
}
