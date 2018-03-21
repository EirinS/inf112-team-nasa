package scenes;

import boardstructure.Move;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import game.Chess;
import game.chessGame.GameInfo;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import game.Checkerboard;

import game.chessGame.ChessGame;

import game.listeners.CheckerboardListener;
import game.listeners.ChessGameListener;

import sprites.PieceSpriteLoader;
import styling.Colors;

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
	private Label topTime, bottomTime;
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
		checkerboard = new Checkerboard(this, sprites, chessGame.getSquares(), this);

		addActors();

		// Perform first AI move if needed.
		chessGame.aiMove();
	}

	private void addActors() {
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
				chessGame.resign();
				SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, game);
				super.clicked(event, x, y);
			}
		});
		int buttonsWidth = (int)quitBtn.getWidth() + 5 + (int)resignBtn.getWidth();

		addActor(quitBtn);
		addActor(resignBtn);

		historyGroup = new VerticalGroup();
		historyGroup.align(Align.top);
		historyScrollPane = new ScrollPane(historyGroup, skin);
		historyScrollPane.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30, checkerboard.getPos() + resignBtn.getHeight() + 125);
		historyScrollPane.setSize(buttonsWidth, checkerboard.getSize() - resignBtn.getHeight() - 200);
		addActor(historyScrollPane);

		String opponent = "Computer";
		if (gameInfo.getLevel() == null) {
			opponent = gameInfo.getOpponent().getNameRating();
		}

		Label topName = new Label(opponent, skin, "title-plain");
		topName.setPosition(historyScrollPane.getX(), checkerboard.getPos() + checkerboard.getSize() - topName.getHeight());
		addActor(topName);

		Label bottomName = new Label(gameInfo.getPlayer().getNameRating(), skin, "title-plain");
		bottomName.setPosition(historyScrollPane.getX(), checkerboard.getPos() + bottomName.getHeight() + 50);
		addActor(bottomName);

		topTime = new Label(chessGame.formatTime(chessGame.getOpponentSeconds()), skin, "title-plain");
		topTime.setPosition(historyScrollPane.getX() + historyScrollPane.getWidth() - topTime.getWidth(), historyScrollPane.getY() + historyScrollPane.getHeight());
		addActor(topTime);

		bottomTime = new Label(chessGame.formatTime(chessGame.getPlayerSeconds()), skin, "title-plain");
		bottomTime.setPosition(historyScrollPane.getX() + historyScrollPane.getWidth() - bottomTime.getWidth(), historyScrollPane.getY() - bottomTime.getHeight());
		addActor(bottomTime);
		setNameColors();
	}

	private void setNameColors() {
		if (chessGame.getTurn() == gameInfo.getPlayerColor()) {
			topTime.setColor(Color.WHITE);
			bottomTime.setColor(Colors.turnColor);
		} else {
			topTime.setColor(Colors.turnColor);
			bottomTime.setColor(Color.WHITE);
		}
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
		setNameColors();
		checkerboard.movePieces(moves);
	}

	@Override
	public void turnTimerElapsed() {
		if (topTime == null || bottomTime == null) return;
		if (chessGame.getTurn() == gameInfo.getPlayerColor()) {
			bottomTime.setText(chessGame.formatTime(chessGame.getPlayerSeconds()));
		} else {
			topTime.setText(chessGame.formatTime(chessGame.getOpponentSeconds()));
		}
	}
}
