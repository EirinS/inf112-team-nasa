package game.listeners;

import boardstructure.Move;

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
}
