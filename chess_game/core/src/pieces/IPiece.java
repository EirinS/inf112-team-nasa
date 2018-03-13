package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;

public interface IPiece {
	
	/**
	 * Find and return all legal positions where a piece can be moved to.
	 * Should check all the rules of the piece and get the legal positions.
	 * It also checks that you cannot move to squares containing a piece 
	 * of your own color.
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
	 * @return true if this piece has moved, false else.
	 */
	public void setMovedFalse();
	
	/**
	 * inPlay = true;
	 */
	public void putInPlay();
	
	
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
	 * Moves a piece from current square to the next, but 
	 * not changing the hasMoved field variable.
	 * @param cur, square moved from
	 * @param next, square moved to
	 * @return null if no piece captured, IPiece piece, if 
	 * piece was captured.
	 */
	public void movePiece(Square cur, Square next);

	/**
	 * Precondition: Assumes you only call this method if you can 
	 * capture a piece, or the position is empty.
	 * Moves a piece to a new position, where it has captured an
	 * enemy piece.
	 * @param cur, the position you move from
	 * @param next, the position you move to
	 * @return the IPiece you captured or null, if space was empty
	 */
	public IPiece captureEnemyPieceAndMovePiece(Square cur, Square next);
	

}
