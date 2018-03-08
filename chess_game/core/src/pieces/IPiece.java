package pieces;

import java.util.ArrayList;

import boardstructure.Square;

public interface IPiece {
	
	/**
	 * Find and return all legal positions where a piece can be moved to.
	 * Should check all the rules of the piece and get the legal positions.
	 * @param Square square, the position of piece on board.
	 * @return ArrayList<Square> of legal positions.
	 */
	public ArrayList<Square> legalPositions(Square square);
	
	/**
	 * @return color of this piece
	 */
	public PieceColor getColor();
	
	/**
	 * @return true if piece is in play, false if piece has been taken.
	 */
	public boolean isInPlay();
	

}
