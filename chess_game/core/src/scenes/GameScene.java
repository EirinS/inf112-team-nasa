package scenes;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import game.Chess;
import game.chessGame.GameInfo;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import game.Checkerboard;

import game.chessGame.ChessGame;
import game.chessGame.GameInfo;

import game.listeners.CheckerboardListener;
import game.listeners.ChessGameListener;
import pieces.PieceColor;
import setups.DefaultSetup;
import sprites.PieceSpriteLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class GameScene extends AbstractScene implements CheckerboardListener, ChessGameListener {

	private Chess game;
	private GameInfo gameInfo;

	private Skin skin;
	private ChessGame chessGame;
	private Checkerboard checkerboard;

	private VerticalGroup historyGroup;
	private ScrollPane historyScrollPane;
	private TextButton quitBtn, resignBtn;

	public GameScene (Chess game, GameInfo gameInfo){
		this.game = game;
		this.gameInfo = gameInfo;
	}

	private void initialize() {

		// Set-up stage
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
		skin = new Skin (Gdx.files.internal("skin/uiskin.json"), atlas);

		// Init ChessGame
		chessGame = new ChessGame(gameInfo, this);

		// Init sprites and checkerboard.
		HashMap<String, Texture> sprites = PieceSpriteLoader.loadDefaultPieces();
		checkerboard = new Checkerboard(this, sprites, chessGame.getBoard().getSquares(), this);

		quitBtn = new TextButton("Quit", skin, "default");
		quitBtn.setSize(quitBtn.getWidth() * 1.5f, quitBtn.getHeight());
		quitBtn.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30, checkerboard.getPos());
		quitBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, game);
				super.clicked(event, x, y);
			}
		});
		resignBtn = new TextButton("Resign", skin, "default");
		resignBtn.setSize(resignBtn.getWidth() * 1.5f, resignBtn.getHeight());
		resignBtn.setPosition(quitBtn.getX() + quitBtn.getWidth() + 5, quitBtn.getY());
		resignBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, game);
				super.clicked(event, x, y);
			}
		});
		int buttonsWidth = (int)quitBtn.getWidth() + 5 + (int)resignBtn.getWidth();
		
		addActor(quitBtn);
		addActor(resignBtn);

		historyGroup = new VerticalGroup();
		//historyGroup.setFillParent(true);
		historyGroup.align(Align.top);
		historyScrollPane = new ScrollPane(historyGroup, skin);
		historyScrollPane.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30, checkerboard.getPos() + resignBtn.getHeight() + 5);
		historyScrollPane.setSize(buttonsWidth, checkerboard.getSize() - (resignBtn.getHeight() + 30));
		addActor(historyScrollPane);
		
		Label header = new Label("History", skin,"title-plain");
		header.setPosition(historyScrollPane.getX() + ((historyScrollPane.getWidth() - header.getWidth()) / 2), historyScrollPane.getY() + historyScrollPane.getHeight() - header.getHeight() + 25);

		addActor(header);
	}

	private void addMoveToHistory(Move m) {
		if (m == null) {
			System.out.println("Should never happend, @addMoveToHistory");
			return;
		}
		Label line = new Label(m.toString(), skin, "title-plain");
		historyGroup.addActor(line);
		historyScrollPane.layout();
		historyScrollPane.scrollTo(0, 0, 0, 0);
	}

	@Override
	public void buildStage() {
		initialize();
	}

	@Override
	public void onDragPieceStarted(int x, int y) {
		ArrayList<Move> legalMoves = chessGame.getLegalMoves(x, y);
		if (!legalMoves.isEmpty()) checkerboard.showMoves(legalMoves);
	}

	@Override
	public void onMoveRequested(int fromX, int fromY, int toX, int toY) {
		chessGame.doTurn(fromX, fromY, toX, toY);
	}

	@Override
	public void illegalMovePerformed(int originX, int originY) {
		checkerboard.movePieceFailed(originX, originY);
	}

	@Override
	public void moveOk(ArrayList<Move> moves) {
		addMoveToHistory(chessGame.getLastMove());
		checkerboard.movePieces(moves);
	}
}
