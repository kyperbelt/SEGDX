package com.segdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.segdx.game.SEGDX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SEGDX.WIDTH;
		config.height = SEGDX.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new SEGDX(), config);
	}
}
