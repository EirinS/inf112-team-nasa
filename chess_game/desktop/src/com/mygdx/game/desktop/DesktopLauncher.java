package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import assistance.GameInformation;
import chessGame.Chess;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Chess(), config);
		config.width = chessGame.GameInformation.WIDTH;
		config.height = chessGame.GameInformation.HEIGHT;
		config.resizable = false; 
	}
}
