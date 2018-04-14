package player;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.PromotionPiece;
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

	/**
	 * Calculates what promotion piece to use.
	 * @param currentBoard Current board
	 * @param promotionMove Move to execute
	 * @return Promotion piece to use.
	 */
	PromotionPiece calculatePromotionPiece(IBoard currentBoard, Move promotionMove);
}