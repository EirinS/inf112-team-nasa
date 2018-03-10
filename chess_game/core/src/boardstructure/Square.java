package boardstructure;

import pieces.IPiece;

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
	 * The piece the square contains (null if nothing)
	 */
	private IPiece piece;
	
	/**
	 * Precondition: Square must be within dimensions of board.
	 * @param x position
	 * @param y position
	 * @param color (black, white)
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * Precondition: Square must be within dimensions of board.
	 * @param x position
	 * @param y position
	 * @param color (black, white)
	 * @param piece, the piece that occupies this square
	 */
	public Square(int x, int y, IPiece piece) {
		this.x = x;
		this.y = y;
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
	public void putPiece(IPiece piece) {
		if (piece == null)
			throw new IllegalArgumentException("Cannot put null piece.");
		if (!isEmpty()) {
			throw new IllegalArgumentException("Square must be empty!");
		}
		this.piece = piece;				
	}
	
	/**
	 * Removes the piece in this sqaure (if there is one, otherwise do nothing)
	 * Sets moved state to true and piece field in square to null.
	 * @return AbstractPiece piece, the piece removed.
	 */
	public IPiece movePiece() {
		IPiece p = piece;
		p.pieceMoved();
		piece = null;
		return p;
	}
	
	/**
	 * Piece is taken by another chess player.
	 * @return the piece removed from the board.
	 */
	public IPiece takePiece() {
		IPiece p = piece;
		p.takePiece();
		piece = null;
		return p;
	}
	
	/**
	 * @return the piece stored in this square
	 */
	public IPiece getPiece() {
		return piece;
	}
	
	@Override
	public String toString() {
		return "Square [x=" + x + ", y=" + y + ", piece=" + piece + "]";
	}
}
