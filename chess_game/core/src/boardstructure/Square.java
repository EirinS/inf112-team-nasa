package boardstructure;

import pieces.AbstractPiece;
import pieces.PieceColor;

public class Square {
	/**
	 * X position
	 */
	private int x;
	
	/**
	 * Y position
	 */
	private int y;
	
	/**
	 * White or black color
	 */
	private BoardColor color;
	
	/**
	 * The piece the square contains (null if nothing)
	 */
	private AbstractPiece piece;
	
	private IBoard board;
	
	/**
	 * Precondition: Square must be within dimensions of board.
	 * @param x position
	 * @param y position
	 * @param color (black, white)
	 */
	public Square(int x, int y, BoardColor color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	/**
	 * Precondition: Square must be within dimensions of board.
	 * @param x position
	 * @param y position
	 * @param color (black, white)
	 * @param piece, the piece that occupies this square
	 */
	public Square(int x, int y, BoardColor color, AbstractPiece piece) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.piece = piece;
	}
	
	/**
	 * @return x position
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return y position
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return color of square (black, white)
	 */
	public BoardColor getBoardColor() {
		return color;
	}
	
	/**
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		if (piece == null)
			return true;
		return false;
	}
	
	/**
	 * Put a piece into the square if it is empty. 
	 * @param piece
	 * @throws IllegalArgumentException, piece must have content and be put into an empty square.
	 */
	public void putPiece(AbstractPiece piece) {
		if (piece == null || !isEmpty())
			throw new IllegalArgumentException("Cannot put null piece");
		this.piece = piece;				
	}
	
	/**
	 * Removes the piece in this sqaure (if there is one, otherwise do nothing)
	 * @return AbstractPiece piece, the piece removed.
	 */
	public AbstractPiece removePiece() {
		AbstractPiece p = piece;
		piece = null;
		return p;
	}
	
	/**
	 * @return the piece stored in this square
	 */
	public AbstractPiece getPiece() {
		return piece;
	}
}
