package game.listeners;

import boardstructure.Move;
import pieces.PieceColor;

import java.util.ArrayList;

public interface ChessGameListener {
	
	/**
	 * Called from ChessGame if you try to move to an illegal position.
	 */
	void illegalMovePerformed(int originX, int originY);

	/**
	 * Called from ChessGame when move has been executed.
	 * @param moves All moves that has been executed on the board.
	 */
	void moveOk(ArrayList<Move> moves);

	/**
	 * Called when the game is over.
	 * @param winLossDraw Integer representing result of game; win = 1, loss = 2, draw = 3.
	 */
	void gameOver(int winLossDraw);

	/**
	 * Called when the turn timer elapses.
	 */
	void turnTimerElapsed();
}
