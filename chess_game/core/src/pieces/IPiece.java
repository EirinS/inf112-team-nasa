package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;

public interface IPiece {
	
	/**
	 * Find and return all legal positions where a piece can be moved to.
	 * Should check all the rules of the piece and get the legal positions.
	 * @param Square square, the position of piece on board.
	 * @param IBoard board, the board we're playing on.
	 * @return ArrayList<Square> of legal positions.
	 */
	public ArrayList<Square> legalPositions(Square square, IBoard board);
	
	/**
	 * @return color of this piece
	 */
	public PieceColor getColor();
	
	/**
	 * @return true if piece is in play, false if piece has been taken.
	 */
	public boolean isInPlay();
	
	/**
	 * Remove piece from play
	 * @return the piece removed from play.
	 */
	public IPiece takePiece();
	

}
