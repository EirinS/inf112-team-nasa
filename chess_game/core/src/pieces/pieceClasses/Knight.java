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
		Square dest = null;

		if (board.withinBoard(x + 1, y + 2)) {
			dest = board.getSquare(x + 1, y + 2);
			ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x + 2, y + 1)) {
			dest = board.getSquare(x + 2, y + 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x + 1, y - 2)) {
			dest = board.getSquare(x + 1, y - 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x + 2, y - 1)) {
			dest = board.getSquare(x + 2, y - 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x - 1, y + 2)) {
			dest = board.getSquare(x - 1, y + 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x - 2, y + 1)) {
			dest = board.getSquare(x - 2, y + 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x - 1, y - 2)) {
			dest = board.getSquare(x - 1, y - 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(x - 2, y - 1)) {
			dest = board.getSquare(x - 2, y - 1);
            ok.add(getMove(origin, dest, board));
		}

		return ok;
	}

	@Override
	public String toString() {
		return "N";
	}
}
