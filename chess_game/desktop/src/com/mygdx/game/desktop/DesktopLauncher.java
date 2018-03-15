package com.mygdx.game.desktop;

import chessGame.GameInformation;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import chessGame.Chess;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Chess(), config);
		config.width = GameInformation.WIDTH;
		config.height = GameInformation.HEIGHT;
		config.resizable = false; 
	}
}
