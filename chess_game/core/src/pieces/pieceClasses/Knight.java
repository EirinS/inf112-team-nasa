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
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(reachableSquares(x, board.getSquare(x, y), board));
		return reachable;

	}

	public ArrayList<Move> reachableSquares(int startPoint, Square origin, IBoard board) {
		ArrayList<Move> ok = new ArrayList<Move>();
		Square dest = null;

		if (board.withinBoard(startPoint + 1, startPoint + 2)) {
			dest = board.getSquare(startPoint + 1, startPoint + 2);
			ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint + 2, startPoint + 1)) {
			dest = board.getSquare(startPoint + 2, startPoint + 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint + 1, startPoint - 2)) {
			dest = board.getSquare(startPoint + 1, startPoint - 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint + 2, startPoint - 1)) {
			dest = board.getSquare(startPoint + 2, startPoint - 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint - 1, startPoint + 2)) {
			dest = board.getSquare(startPoint - 1, startPoint + 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint - 2, startPoint + 1)) {
			dest = board.getSquare(startPoint - 2, startPoint + 1);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint - 1, startPoint - 2)) {
			dest = board.getSquare(startPoint - 1, startPoint - 2);
            ok.add(getMove(origin, dest, board));
		}

		if (board.withinBoard(startPoint - 2, startPoint - 1)) {
			dest = board.getSquare(startPoint - 2, startPoint - 1);
            ok.add(getMove(origin, dest, board));
		}

		return ok;
	}

	@Override
	public String toString() {
		return "N";
	}
}
