package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import register.RegisteredPlayers;
import scenes.MainMenuScene;
import com.badlogic.gdx.*;
import scenes.SceneEnum;
import scenes.SceneManager;


public class Chess extends Game {
	private SpriteBatch batch;
	private static RegisteredPlayers filehandler = new RegisteredPlayers("playerfile.txt");
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		SceneManager.getInstance().initialize(this);
		SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, this);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public SpriteBatch getSpriteBatch(){
		return this.batch;
	}
	
	public static RegisteredPlayers getRegisteredPlayers()
	{
		return filehandler;
	}
}
