package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Knight extends AbstractPiece {

	public Knight(PieceColor color) {
		inPlay = true;
		this.color = color;
	}
	
	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
		reachable.addAll(reachableSquares(x, board));
		return reachable;

	}

	public ArrayList<Square> reachableSquares(int startPoint, IBoard board) {
		ArrayList<Square> ok = new ArrayList<Square>();
		Square newSq;

		if (board.withinBoard(startPoint + 1, startPoint + 2)) {
			newSq = board.getSquare(startPoint + 1, startPoint + 2);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint + 2, startPoint + 1)) {
			newSq = board.getSquare(startPoint + 2, startPoint + 1);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint + 1, startPoint - 2)) {
			newSq = board.getSquare(startPoint + 1, startPoint - 2);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint + 2, startPoint - 1)) {
			newSq = board.getSquare(startPoint + 2, startPoint - 1);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint - 1, startPoint + 2)) {
			newSq = board.getSquare(startPoint - 1, startPoint + 2);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint - 2, startPoint + 1)) {
			newSq = board.getSquare(startPoint - 2, startPoint + 1);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint - 1, startPoint - 2)) {
			newSq = board.getSquare(startPoint - 1, startPoint - 2);
			ok.add(newSq);
		}

		if (board.withinBoard(startPoint - 2, startPoint - 1)) {
			newSq = board.getSquare(startPoint - 2, startPoint - 1);
			ok.add(newSq);
		}

		return ok;
	}

	public String toString() {
		return "N";
	}

}
