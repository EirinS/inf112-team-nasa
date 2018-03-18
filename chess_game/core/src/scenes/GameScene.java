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
import styling.Colors;

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
	private ArrayList<Move> selectedMoves;

	// TODO: 15.03.2018 this is temp; just to have something to draw.
	private String player1 = "triki";
	private String player2 = "wagle";
	private PieceColor turn;

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
		selectedMoves = new ArrayList<>();
		turn = PieceColor.WHITE;
		checkerboard = new Checkerboard(game, stage, new GameInfo(PieceColor.WHITE, player1, player2, sprites, board.getSquares()), this); // TODO: 18/03/2018 make parameters dynamic
	}

	private void changeTurn() {
		if (turn == PieceColor.WHITE) {
			turn = PieceColor.BLACK;
		} else {
			turn = PieceColor.WHITE;
		}
		selectedSquare = null;
		selectedMoves.clear();
		checkerboard.showMoves(null, null);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
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
		System.out.println(x + ", " + y);
		Square square = board.getSquare(x, y);
		if (square.getPiece().getColor() != turn) return; // Ignore if we click on opponent pieces.
		if (selectedSquare == null || !selectedSquare.equals(square)) {
			selectedSquare = square;
			selectedMoves = selectedSquare.getPiece().getLegalMoves(selectedSquare, board, PieceColor.WHITE);
			checkerboard.showMoves(selectedSquare, selectedMoves);
		} else if (selectedSquare.equals(square)) {
			selectedSquare = null;
		}
	}

	@Override
	public void onMoveRequested(Move m) {
		Move move = board.move(m);
		if (move == null) {

			// TODO: 18/03/2018 show this in the gui
			System.out.println("Move is illegal!");
		} else {
			checkerboard.movePiece(m);
		}
	}
}
