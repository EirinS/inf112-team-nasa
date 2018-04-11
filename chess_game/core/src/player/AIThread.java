package player;

import boardstructure.IBoard;
import boardstructure.Move;
import game.chessGame.ChessGame;

/**
 * Created by jonas on 11/04/2018.
 */
public class AIThread implements Runnable {

	private AI ai;
	private IBoard board;
	private ChessGame chessGame;

	public AIThread(AI ai, IBoard board, ChessGame chessGame) {
		this.ai = ai;
		this.board = board;
		this.chessGame = chessGame;
	}

	/**
	 * Calculates move with a given AI asynchronous
	 */
	@Override
	public synchronized void run() {
		Move move = ai.calculateMove(board);
		chessGame.doTurn(move.getFrom().getX(), move.getFrom().getY(), move.getTo().getX(), move.getTo().getY());
	}

}
