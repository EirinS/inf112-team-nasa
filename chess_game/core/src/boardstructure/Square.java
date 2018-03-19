package boardstructure;

import pieces.IPiece;

import java.util.Objects;

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
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}


	String numberToLetter(int num){
		switch (num) {
		case 7:
			return "H";
		case 6:
			return "G";
		case 5:
			return "F";
		case 4:
			return "E";
		case 3:
			return "D";
		case 2:
			return "C";
		case 1:
			return "B";
		default:
			return "A";

		}
	}

	private int flipY(int num) {
		switch (num) {
		case 7:
			return 1;
		case 6:
			return 2;
		case 5:
			return 3;
		case 4:
			return 4;
		case 3:
			return 5;
		case 2:
			return 6;
		case 1:
			return 7;
		default:
			return 8;

		}
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
	 * Removes the piece in this square (if there is one, otherwise do nothing)
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
		return numberToLetter(x)+flipY(y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Square square = (Square) o;
		return x == square.x &&
				y == square.y &&
				Objects.equals(piece, square.piece);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, piece);
	}
}