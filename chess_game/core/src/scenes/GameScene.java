package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;

import chessGame.Chess;


public class GameScene implements Screen {

	private Chess game;
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
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

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
