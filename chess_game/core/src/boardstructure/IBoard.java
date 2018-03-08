package boardstructure;

import java.util.ArrayList;

public interface IBoard {
	
	/**
	 * Width of board
	 * @return width
	 */
	int getWidth();
	
	/**
	 * Height of board
	 * @return height
	 */
	int getHeight();
	
	/**
	 * Get a specific square in board
	 * @return square
	 */
	public Square getSquare(int x, int y);
	
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
	public boolean moveable(Square sq);
	
	
	
	
	
}
