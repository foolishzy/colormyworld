package com.foolishzy.colormyworld.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.foolishzy.colormyworld.ColorMyWorldGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = ColorMyWorldGame.TITLE;
//		config.height = ColorMyWorldGame.V_HEIGHT;
//		config.width = ColorMyWorldGame.V_WIDTH;
		config.width = 800;
		config.height = 400;
		new LwjglApplication(new ColorMyWorldGame(), config);
	}
}
