package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Knight extends AbstractPiece {

	public Knight(PieceColor color) {
		inPlay = true;
		this.color = color;
	}
	
	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(reachableSquares(x, y, board.getSquare(x, y), board));
		return reachable;

	}

	public ArrayList<Move> reachableSquares(int x, int y, Square origin, IBoard board) {
		ArrayList<Move> ok = new ArrayList<Move>();

		if (board.withinBoard(x + 1, y + 2)) {
			Move m = getMove(origin, x + 1, y + 2, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x + 2, y + 1)) {
			Move m = getMove(origin, x + 2, y + 1, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x + 1, y - 2)) {
			Move m = getMove(origin, x + 1, y - 2, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x + 2, y - 1)) {
			Move m = getMove(origin, x + 2, y - 1, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x - 1, y + 2)) {
			Move m = getMove(origin, x - 1, y + 2, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x - 2, y + 1)) {
			Move m = getMove(origin, x - 2, y + 1, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x - 1, y - 2)) {
			Move m = getMove(origin, x - 1, y - 2, board);
			if (m != null) ok.add(m);
		}

		if (board.withinBoard(x - 2, y - 1)) {
			Move m = getMove(origin, x - 2, y - 1, board);
			if (m != null) ok.add(m);
		}

		return ok;
	}

	@Override
	public String toString() {
		return "N";
	}
}
