package board_logic;

import piece_logic.PieceColor;

public class Square {
	private int row;
	private int column;
	private PieceColor pieceColor;

	public Square(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public PieceColor getPieceColor() {
		return pieceColor;
	}
}
