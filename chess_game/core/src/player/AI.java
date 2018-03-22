package player;

import boardstructure.IBoard;
import boardstructure.Move;
import pieces.PieceColor;

/**
 * Created by jonas on 12/03/2018.
 */
public interface AI {

	/**
	 * Calculates the best move and returns it.
	 * @param currentBoard Board we're using.
	 * @return Best move.
	 */
	Move calculateMove(IBoard currentBoard);

	/**
	 * Gets the AI piece color
	 * @return Piece color.
	 */
	PieceColor getPieceColor();
	
	/**
	 * @return int rating of this AI
	 */
	int getRating();
}