package scenes;

import boardstructure.Board;
import boardstructure.BoardListener;
import boardstructure.Move;

import boardstructure.PromotionPiece;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import game.Chess;
import game.WindowInformation;
import game.chessGame.GameInfo;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import game.Checkerboard;

import game.chessGame.ChessGame;

import game.listeners.CheckerboardListener;
import game.listeners.ChessGameListener;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Queen;
import pieces.pieceClasses.Rook;
import player.AI;
import player.AIHard;

import player.AIThreadHint;
import sound.AudioManager;
import sprites.PieceSpriteLoader;
import styling.Colors;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScene extends AbstractScene implements CheckerboardListener, ChessGameListener, BoardListener {

	private Chess game;
	private GameInfo gameInfo;

	private Skin skin;
	private ChessGame chessGame;
	private Checkerboard checkerboard;

	private VerticalGroup historyGroup;
	private ScrollPane historyScrollPane, promotionTable;
	private Label topTime, bottomTime;

	private HashMap<String, Texture> sprites;
	private TextButton quitBtn, resignBtn, queenBtn, bishopBtn, knightBtn, rookBtn, hintBtn, muteBtn, undoBtn;

	private Move promotionMove;
	private Group promotionDialog;

	public GameScene(Chess game, GameInfo gameInfo) {
		this.game = game;
		this.gameInfo = gameInfo;
	}

	private void initialize() {

		// Set-up stage
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

		Image imgBackground = new Image(new Texture("board/game_bg.png"));
		imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);
		addActor(imgBackground);

		// Init ChessGame
		chessGame = new ChessGame(gameInfo, this);

		// Init sprites and checkerboard.
		this.sprites = PieceSpriteLoader.loadDefaultPieces();
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
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, game);
				super.touchUp(event, x, y, pointer, button);
			}
		});

		resignBtn = new TextButton("Resign", skin, "default");
		resignBtn.setSize(resignBtn.getWidth() * 1.5f, resignBtn.getHeight());
		resignBtn.setPosition(quitBtn.getX() + quitBtn.getWidth() + 5, quitBtn.getY());
		resignBtn.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				chessGame.resign();
				super.touchUp(event, x, y, pointer, button);
			}
		});

		hintBtn = new TextButton("Hint", skin, "default");
		hintBtn.setSize(hintBtn.getWidth() * 1.5f, hintBtn.getHeight());
		hintBtn.setPosition(quitBtn.getX(), checkerboard.getPos() + 40);
		hintBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				AI ai = new AIHard(chessGame.getBoard().getTurn());
				AIThreadHint tAi = new AIThreadHint(chessGame.getBoard(), ai, checkerboard);
				Thread thread = new Thread(tAi);
				thread.start();
				super.clicked(event, x, y);
			}
		});

		muteBtn = new TextButton("Mute", skin, "toggle");
		muteBtn.setSize(muteBtn.getWidth() * 1.5f, muteBtn.getHeight());
		muteBtn.setPosition(hintBtn.getX() + hintBtn.getWidth() + 5, checkerboard.getPos() + 40);
		muteBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				AudioManager.toggle();
				super.clicked(event, x, y);
			}
		});
		if (!AudioManager.audioOn())
			muteBtn.toggle();

		int buttonsWidth = (int) quitBtn.getWidth() + 5 + (int) resignBtn.getWidth();

		addActor(quitBtn);
		addActor(resignBtn);
		addActor(hintBtn);
		addActor(muteBtn);

		historyGroup = new VerticalGroup();
		historyGroup.align(Align.top);
		historyScrollPane = new ScrollPane(historyGroup, skin);
		historyScrollPane.setPosition(checkerboard.getPos() + checkerboard.getSize() + 30,
				checkerboard.getPos() + resignBtn.getHeight() + 125);
		historyScrollPane.setSize(buttonsWidth, checkerboard.getSize() - resignBtn.getHeight() - 200);
		addActor(historyScrollPane);

		String opponent = "Computer";
		if (gameInfo.getLevel() == null) {
			opponent = gameInfo.getOpponent().getNameRating();
		}

		Label topName = new Label(opponent, skin, "title-plain");
		topName.setPosition(historyScrollPane.getX(),
				checkerboard.getPos() + checkerboard.getSize() - topName.getHeight());
		addActor(topName);

		Label bottomName = new Label(gameInfo.getPlayer().getNameRating(), skin, "title-plain");
		bottomName.setPosition(historyScrollPane.getX(), checkerboard.getPos() + bottomName.getHeight() + 50);
		addActor(bottomName);

		topTime = new Label(chessGame.formatTime(chessGame.getOpponentSeconds()), skin, "title-plain");
		topTime.setPosition(historyScrollPane.getX() + historyScrollPane.getWidth() - topTime.getWidth(),
				historyScrollPane.getY() + historyScrollPane.getHeight());
		addActor(topTime);

		bottomTime = new Label(chessGame.formatTime(chessGame.getPlayerSeconds()), skin, "title-plain");
		bottomTime.setPosition(historyScrollPane.getX() + historyScrollPane.getWidth() - bottomTime.getWidth(),
				historyScrollPane.getY() - bottomTime.getHeight());
		addActor(bottomTime);
		setNameColors();

		if (chessGame.getGameInfo().isSinglePlayer()) {
			undoBtn = new TextButton("Undo", skin, "default");
			undoBtn.setSize(undoBtn.getWidth() * 1.5f, undoBtn.getHeight());
			undoBtn.setPosition(historyScrollPane.getX() + 5, historyScrollPane.getY() - bottomTime.getHeight());
			undoBtn.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (chessGame.undoTurn()) {
						removeLastMove();
						checkerboard.setThisCheckerBoard(chessGame.getBoard());
					}
					super.clicked(event, x, y);
				}
			});

			addActor(undoBtn);
		}
		addPromotionActors();
	}

	private void addPromotionActors() {
		promotionDialog = new Group();
		promotionDialog.setVisible(false);

		queenBtn = new TextButton("Queen", skin, "default");
		queenBtn.setSize(queenBtn.getWidth() * 1.5f, queenBtn.getHeight());
		queenBtn.setPosition(checkerboard.getPos(), checkerboard.getPos() + checkerboard.getSize() / 2);
		queenBtn.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				promotionDialog.setVisible(false);
				chessGame.performPromotion(promotionMove, PromotionPiece.QUEEN);
				promotionMove = null;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		bishopBtn = new TextButton("Bishop", skin, "default");
		bishopBtn.setSize(bishopBtn.getWidth() * 1.5f, bishopBtn.getHeight());
		bishopBtn.setPosition(checkerboard.getPos() + queenBtn.getWidth() + 5,
				checkerboard.getPos() + checkerboard.getSize() / 2);
		bishopBtn.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				promotionDialog.setVisible(false);
				chessGame.performPromotion(promotionMove, PromotionPiece.BISHOP);
				promotionMove = null;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		rookBtn = new TextButton("Rook", skin, "default");
		rookBtn.setSize(rookBtn.getWidth() * 1.5f, rookBtn.getHeight());
		rookBtn.setPosition(checkerboard.getPos() + queenBtn.getWidth() + bishopBtn.getWidth() + 10,
				checkerboard.getPos() + checkerboard.getSize() / 2);
		rookBtn.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				promotionDialog.setVisible(false);
				chessGame.performPromotion(promotionMove, PromotionPiece.ROOK);
				promotionMove = null;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		knightBtn = new TextButton("Knight", skin, "default");
		knightBtn.setSize(knightBtn.getWidth() * 1.5f, knightBtn.getHeight());
		knightBtn.setPosition(
				checkerboard.getPos() + queenBtn.getWidth() + bishopBtn.getWidth() + rookBtn.getWidth() + 15,
				checkerboard.getPos() + checkerboard.getSize() / 2);
		knightBtn.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				promotionDialog.setVisible(false);
				chessGame.performPromotion(promotionMove, PromotionPiece.KNIGHT);
				promotionMove = null;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		VerticalGroup buttons = new VerticalGroup();
		buttons.addActor(queenBtn);
		buttons.addActor(bishopBtn);
		buttons.addActor(rookBtn);
		buttons.addActor(knightBtn);

		Label headline = new Label("Choose promotion\n", skin);

		// dette ble veldig rart
		// headline.setFontScale((float) 1.3);

		promotionTable = new ScrollPane(buttons, skin);
		promotionTable.setPosition(checkerboard.getPos() + checkerboard.getSize() / 2 - promotionTable.getWidth() / 2,
				checkerboard.getPos() + checkerboard.getSize() / 2 - promotionTable.getWidth() / 2);
		promotionTable.setSize(headline.getWidth() + 50, 4 * queenBtn.getHeight() + headline.getHeight() + 50);
		promotionDialog.addActor(promotionTable);

		headline.setPosition(checkerboard.getPos() + 30,
				checkerboard.getPos() + checkerboard.getSize() / 2 + promotionTable.getHeight());
		headline.setSize(headline.getWidth(), headline.getHeight());
		promotionDialog.addActor(headline);
		buttons.addActorAt(0, headline);

		addActor(promotionDialog);
	}

	private void showPromotionOptions() {
		if (promotionMove == null) {
			System.out.println("Promotion move empty... check code.");
			return;
		}
		promotionDialog.setVisible(true);
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

	private void removeLastMove() {
		if (chessGame.getTurn() == chessGame.getGameInfo().getPlayerColor()) {
			historyGroup.removeActor(historyGroup.getChildren().get(historyGroup.getChildren().size - 1));
		}
		historyGroup.removeActor(historyGroup.getChildren().get(historyGroup.getChildren().size - 1));
	}

	private boolean shouldAnimateMove() {
		return chessGame.getTurn() == chessGame.getGameInfo().getPlayerColor();
	}

	@Override
	public void buildStage() {
		initialize();
	}

	@Override
	public void onDragPieceStarted(int x, int y) {
		ArrayList<Move> legalMoves = chessGame.getLegalMoves(x, y);
		checkerboard.showMoves(legalMoves);
	}

	@Override
	public void onMoveRequested(int fromX, int fromY, int toX, int toY) {
		chessGame.doTurn(fromX, fromY, toX, toY);
	}

	@Override
	public void illegalMovePerformed(int originX, int originY) {
		checkerboard.movePieceFailed(originX, originY, shouldAnimateMove());
	}

	@Override
	public void moveOk(ArrayList<Move> moves) {
		addMoveToHistory(chessGame.getLastMove());
		setNameColors();
		checkerboard.movePieces(moves, shouldAnimateMove());

		// when move is done, show move for opponent to see which piece moved
		for (Move m : moves)
			checkerboard.showPrevMove(m);
	}

	@Override
	public void promotionRequested(Move m) {
		promotionMove = m;
		showPromotionOptions();
	}

	@Override
	public void movePerformed(Board board, ArrayList<Move> moves) {
	}

	@Override
	public void gameOver(int winLossDraw) {
		switch (winLossDraw) {
		case 1:
			SceneManager.getInstance().showScreen(SceneEnum.VICTORY, game, gameInfo, true);
			break;
		case 2:
			SceneManager.getInstance().showScreen(SceneEnum.VICTORY, game, gameInfo, false);
			break;
		case 3:
			SceneManager.getInstance().showScreen(SceneEnum.VICTORY, game, gameInfo, null);
			break;
		}
	}

	@Override
	public void turnTimerElapsed() {
		if (topTime == null || bottomTime == null)
			return;
		// no reason to not update both??
		// if (chessGame.getTurn() == gameInfo.getPlayerColor()) {
		bottomTime.setText(chessGame.formatTime(chessGame.getPlayerSeconds()));
		// } else {
		topTime.setText(chessGame.formatTime(chessGame.getOpponentSeconds()));
		// }
	}
}
