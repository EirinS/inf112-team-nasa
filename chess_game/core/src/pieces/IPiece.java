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
	
	/**
	 * When a piece moves, this sets hasMoved to true.
	 */
	public void pieceMoved();
	
	/**
	 * @return true if this piece has moved, false else.
	 */
	public boolean hasMoved();
	
	
	/**
	 * Finds all enemy pieces reached by this piece, and with the color given.
	 * @param x-coordinate of current piece
	 * @param y-position of current piece
	 * @param board
	 * @param PieceColor opponent, the color of the pieces you'll look for.
	 * @return ArrayList<IPiece> enemy pieces reached by this piece
	 */
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent);
	
	
	/**
	 * Finds all empty squares that can be reached by this rook (empty or
	 * to capture another piece).
	 * Hence, you can move to it, if it doesn't leave your king in check
	 * @param x-coordinate
	 * @param y-coordinate
	 * @param board
	 * @return all empty squares reached by this rook
	 */
	public ArrayList<Square> getMovableSquares(int x, int y, IBoard board);
	
	/**
	 * Moves a piece from current square to the next.
	 * @param cur, square moved from
	 * @param next, square moved to
	 */
	public void movePiece(Square cur, Square next);
	

}
