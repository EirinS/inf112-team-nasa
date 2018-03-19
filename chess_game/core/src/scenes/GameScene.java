package scenes;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import game.CheckerboardListener;
import game.Chess;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.Checkerboard;
import game.GameInfo;
import pieces.PieceColor;
import setups.DefaultSetup;
import sprites.PieceSpriteLoader;

import java.util.HashMap;


public class GameScene implements Screen, CheckerboardListener {

	private Chess game;
	private Stage stage;

	private Skin skin;

	private HashMap<String, Texture> sprites;
	private Checkerboard checkerboard;
	private Board board;

	// TODO: 15.03.2018 this is temp; just to have something to draw.
	private String player1 = "triki";
	private String player2 = "wagle";
	private PieceColor turn;

	private VerticalGroup historyGroup;
	private TextButton quitBtn, resignBtn;

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
		turn = PieceColor.WHITE;
		checkerboard = new Checkerboard(game, stage, new GameInfo(PieceColor.WHITE, player1, player2, sprites, board.getSquares()), this); // TODO: 18/03/2018 make parameters dynamic
	
		quitBtn = new TextButton("Quit", skin, "default");
		quitBtn.setSize(quitBtn.getWidth() * 1.5f, quitBtn.getHeight());
		quitBtn.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30, checkerboard.getPos());
		resignBtn = new TextButton("Resign", skin, "default");
		resignBtn.setSize(resignBtn.getWidth() * 1.5f, resignBtn.getHeight());
		resignBtn.setPosition(quitBtn.getX() + quitBtn.getWidth() + 5, quitBtn.getY());
		int buttonsWidth = (int)quitBtn.getWidth() + 5 + (int)resignBtn.getWidth();
		
		stage.addActor(quitBtn);
		stage.addActor(resignBtn);

		historyGroup = new VerticalGroup();
		historyGroup.setFillParent(true);
		ScrollPane scrollPane = new ScrollPane(historyGroup, skin);
		scrollPane.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30, checkerboard.getPos() + resignBtn.getHeight() + 5);
		scrollPane.setSize(buttonsWidth, checkerboard.getSize() - (resignBtn.getHeight() + 30));
		stage.addActor(scrollPane);
		
		Label header = new Label("History", skin,"title-plain");
		header.setPosition(scrollPane.getX() + ((scrollPane.getWidth() - header.getWidth()) / 2), scrollPane.getY() + scrollPane.getHeight() - header.getHeight() + 25);
		
		stage.addActor(header);
	}

	private void changeTurn() {
		if (turn == PieceColor.WHITE) {
			turn = PieceColor.BLACK;
		} else {
			turn = PieceColor.WHITE;
		}
	}

	private void addMoveToHistory(Move m) {
		if (m == null) {
			System.out.println("Should never happend, @addMoveToHistory");
			return;
		}
		Label line = new Label(m.toString(), skin, "title-plain");
		line.setAlignment(Align.left);
		historyGroup.addActor(line);
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
	public void onDragPieceStarted(int x, int y) {
		Square square = board.getSquare(x, y);
		if (square.getPiece().getColor() != turn) return; // Ignore if we click on opponent pieces.
		checkerboard.showMoves(square.getPiece().getLegalMoves(square, board, PieceColor.WHITE));
	}

	@Override
	public void onMoveRequested(int fromX, int fromY, int toX, int toY) {
		Move move = board.move(fromX, fromY, toX, toY);
		if (move == null) {
			checkerboard.movePieceFailed(fromX, fromY);
		} else {
			addMoveToHistory(board.getLastMove());
			checkerboard.movePiece(fromX, fromY, toX, toY);
		}
	}
}
