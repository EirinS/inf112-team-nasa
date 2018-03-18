package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import scenes.MainMenuScene;

import com.badlogic.gdx.*;

public class Chess extends Game {
	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScene(this));
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
