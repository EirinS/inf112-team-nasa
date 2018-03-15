package scenes;

import chessGame.GameInformation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import chessGame.Chess;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import gui.Checkerboard;
import scenes.styling.Colors;
import scenes.styling.Styles;

import static chessGame.GameInformation.HEIGHT;


public class GameScene implements Screen {

	private Chess game;
	private Stage stage;
	//private Sprite bPawn, wPawn, bRook, wRook, wBishop, bBishop, bKnight, wKnight, bQueen, wQueen;
	private Button btn;
	private Skin skin;
	private TiledMap checkerboard;
	private OrthogonalTiledMapRenderer checkerboardRenderer;

	// TODO: 15.03.2018 this is temp; just to have something to draw.
	private String player1 = "triki";
	private String player2 = "wagle";

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
		initialize();
		addActors();
		loadCheckerboard();
	}

	private void loadCheckerboard() {
		checkerboard = Checkerboard.getCheckerboard();
		checkerboardRenderer = new OrthogonalTiledMapRenderer(checkerboard, game.getSpriteBatch());
	}

	private void initialize() {
		stage = new Stage(new ScreenViewport());
		skin = new Skin();
		Styles.myriadProFont(skin);
		Styles.blueButton(skin);
		Gdx.input.setInputProcessor(stage);
	}

	private void addActors() {
		btn = new TextButton("Test", skin);
		stage.addActor(btn);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, Colors.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getSpriteBatch().begin();

		skin.getFont("white-24").draw(game.getSpriteBatch(), player1, 100, HEIGHT - 50);
		skin.getFont("white-24").draw(game.getSpriteBatch(), player2, 300, HEIGHT - 50);

		//checkerboardRenderer.render();

		game.getSpriteBatch().end();

		stage.act(delta);
		stage.draw();
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
	}
}
