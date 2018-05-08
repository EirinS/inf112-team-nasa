package com.mygdx.game.desktop;

import game.WindowInformation;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

//import animation.CustomAnimation;
import game.Chess;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Chess(), config);
		//new CustomAnimation("pictures/loading.png", 10, 474, 717, 5, 8, 10),
		config.width = WindowInformation.WIDTH;
		config.height = WindowInformation.HEIGHT;
		config.resizable = false;
		
		//https://www.freeiconspng.com/img/11288
		config.addIcon("pictures/chess32.png", FileType.Internal);
		config.addIcon("pictures/chess128.png", FileType.Internal);
	}
}
