package player;

import boardstructure.IBoard;
import boardstructure.Move;
import com.badlogic.gdx.Gdx;
import game.chessGame.ChessGame;

/**
 * Created by jonas on 11/04/2018.
 */
public class AIThreadMove implements Runnable {

	private AI ai;
	private IBoard board;
	private ChessGame chessGame;

	public AIThreadMove(AI ai, IBoard board, ChessGame chessGame) {
		this.ai = ai;
		this.board = board;
		this.chessGame = chessGame;
	}

	/**
	 * Calculates move with a given AI asynchronous
	 */
	@Override
	public synchronized void run() {

		Gdx.app.postRunnable(
				new Runnable() {
					@Override
					public void run() {
						Move move = ai.calculateMove(board);
						chessGame.doTurn(move.getFrom().getX(), move.getFrom().getY(), move.getTo().getX(), move.getTo().getY());
					}
				}
		);
	}

}
