package boardstructure;

import java.util.ArrayList;

import pieces.IPiece;

public interface IBoard {
	
	/**
	 * Width of board
	 * @return width
	 */
	public int getWidth();
	
	/**
	 * Height of board
	 * @return height
	 */
	public int getHeight();
	
	/**
	 * Dimensions of board.
	 * @return dimension
	 */
	public int getDimension();
	
	/**
	 * Get a specific square in board
	 * @return square
	 */
	public Square getSquare(int x, int y);
	
	/**
	 * Get the integer value, that is the index of the
	 * square in the ArrayList representation of board.
	 * @param x
	 * @param y
	 * @return index in ArrayList representation of board.
	 */
	public int getBoardPlacement(Square sq);
	
	/**
	 * Gives all the squares in the board.
	 * @return all squares in board.
	 */
	public ArrayList<Square> getBoard();
	
	/**
	 * Check if a piece is inside the board, and if there is no other piece there.
	 * @param sq, square you are checking
	 * @return true if legal move, false else
	 */
	public boolean movable(Square sq);
	
	/**
	 * Add a square in the square's given position.
	 * @param sq
	 */
	public void addSquare(Square sq);
	
	/**
	 * Is this position within the board?
	 * @param x, x-coordinate
	 * @param y, y-coordinate
	 * @return true if within board, false if not.
	 */
	public boolean withinBoard(Square sq);
	
	/**
	 * Returns all the pieces than can be captured by white.
	 * @return ArrayList<IPiece>, all pieces threatened by white.
	 */
	public ArrayList<IPiece> piecesThreatenedByWhite();
	
	/**
	 * Returns all the pieces than can be captured by black.
	 * @return ArrayList<IPiece>, all pieces threatened by black.
	 */
	public ArrayList<IPiece> piecesThreatenedByBlack();
	
	
	
	
}
