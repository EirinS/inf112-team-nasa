package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import register.RegisteredPlayers;
import scenes.MainMenuScene;

import com.badlogic.gdx.*;
import scenes.SceneEnum;
import scenes.SceneManager;

public class Chess extends Game {
	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		RegisteredPlayers filehandler = new RegisteredPlayers("playerfile.txt");
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
}
