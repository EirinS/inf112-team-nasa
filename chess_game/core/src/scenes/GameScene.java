package scenes;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;

import game.CheckerboardListener;
import game.Chess;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.Checkerboard;
import game.GameInfo;
import pieces.PieceColor;
import setups.DefaultSetup;
import sprites.PieceSpriteLoader;
import scenes.styling.Colors;

import java.util.ArrayList;
import java.util.HashMap;


public class GameScene implements Screen, CheckerboardListener {

	private Chess game;
	private Stage stage;

	private Skin skin;

	private HashMap<String, Texture> sprites;
	private Checkerboard checkerboard;
	private Board board;
	private Square selectedSquare;

	// TODO: 15.03.2018 this is temp; just to have something to draw.
	private String player1 = "triki";
	private String player2 = "wagle";

	public GameScene (Chess mainGame){
		game = mainGame;
		initialize();
	}

	private void initialize() {

		// Set-up stage
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
		skin = new Skin (Gdx.files.internal("skin/uiskin.json"), atlas);
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		// Init sprites and checkerboard.
		sprites = PieceSpriteLoader.loadDefaultPieces();
		board = (new DefaultSetup()).getInitialPosition(PieceColor.WHITE);
		checkerboard = new Checkerboard(game, stage, new GameInfo(PieceColor.WHITE, player1, player2, sprites, board.getSquares()), this); // TODO: 18/03/2018 make parameters dynamic
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(Colors.bgColor.r, Colors.bgColor.g, Colors.bgColor.b, Colors.bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

	@Override
	public void onPieceClick(int x, int y) {
		System.out.println("piece x: " + x + ", y: " + y + " clicked.");
		Square square = board.getSquare(x, y);
		if (selectedSquare == null || !selectedSquare.equals(square)) {
			selectedSquare = square;
			ArrayList<Move> moves = selectedSquare.getPiece().getLegalMoves(selectedSquare, board, PieceColor.WHITE);
			if (moves.isEmpty()) System.out.println("No moves?");
			for (Move m : moves) {
				System.out.println(m.getTo().getX() + ", " + m.getTo().getY());
			}
		} else if (selectedSquare.equals(square)) {
			selectedSquare = null;
		}
	}
}
