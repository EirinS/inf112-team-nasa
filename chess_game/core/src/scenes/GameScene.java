package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import chessGame.Chess;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class GameScene implements Screen {

	private Chess game;
	private Stage stage;
	//private Sprite bPawn, wPawn, bRook, wRook, wBishop, bBishop, bKnight, wKnight, bQueen, wQueen;

	public GameScene (Chess mainGame){

		game = mainGame;
		/*
		 * bPawn = new Sprite(new Texture("bPawn.jpg");
		 * wPawn = new Sprite(new Texture("wPawn.jpg");
		 * bRook = new Sprite(new Texture("bRook.jpg");
		 * wRook = new Sprite(new Texture("wRook.jpg");
		 * wBishop = new Sprite(new Texture("wBishop.jpg");
		 * bBishop = new Sprite(new Texture("bBishop.jpg");
		 * bKnight = new Sprite(new Texture("bKnight.jpg");
		 * wKnight = new Sprite(new Texture("wKnight.jpg");
		 * bQueen = new Sprite (new Texture("bQueen.jpg");
		 * wQueen = new Sprite (new Texture("wQueen.jpg");
		 */
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getSpriteBatch().begin();
		stage.act(delta);
		stage.draw();
		game.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
