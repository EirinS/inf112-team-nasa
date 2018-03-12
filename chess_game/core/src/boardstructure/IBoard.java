package boardstructure;

import java.util.ArrayList;

import pieces.IPiece;
import pieces.PieceColor;

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
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean withinBoard(int x, int y);
	
	
	/**
	 * Returns all the pieces than can be captured by white.
	 * @param PieceColor player, your piece color
	 * @param PieceColor opponent, the color that threatens you.
	 * @return ArrayList<IPiece>, all pieces threatened by white.
	 */
	public ArrayList<IPiece> piecesThreatenedByOpponent(PieceColor player, PieceColor opponent);
	
	/**
	 * Move a piece to a legal position on the board. 
	 * Assumes the piece chosen is a piece of correct color.
	 * @param start, the position the piece had
	 * @param end, the position the piece goes to.
	 * @param PieceColor player, the color of the player that moves piece.
	 * @return algebraic notation of the move. 
	 */
	public String move(Square start, Square end, PieceColor player);
	
	/**
	 * This method returns the algebraic notation of all moves made.
	 * @return ArrayList<String> history of all moves made.
	 */
	public ArrayList<String> getHistory();

	
	
}
