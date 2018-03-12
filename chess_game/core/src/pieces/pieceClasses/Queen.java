package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Queen extends AbstractPiece {
	
	public Queen(PieceColor color) {
		inPlay = true;
		this.color = color;
	}

	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
		reachable.addAll(reachableSquares(x, y, board, true, false));
		reachable.addAll(reachableSquares(y, x, board, false, true));
		reachable.addAll(reachableSquares(x, y, board, false, false));
		return reachable;
		
	}

	public ArrayList<Square> reachableSquares(int startPoint, int axis, IBoard board, boolean horizontal,
			boolean diagonal) {
		ArrayList<Square> ok = new ArrayList<Square>();
		Square newSq;

		for (int i = startPoint + 1; i < board.getDimension(); i++) {
			if (horizontal) {
				newSq = board.getSquare(i, axis);
			} else if (diagonal) {
				newSq = board.getSquare(i, i);
			} else {
				newSq = board.getSquare(axis, i);
			}
			
			if (!newSq.isEmpty()) {
				if (getColor() != newSq.getPiece().getColor())
					ok.add(newSq);
				break;
			} else {
				ok.add(newSq);
			}
		}
		
		for (int i = startPoint - 1; i >= 0; i--) {
			if (horizontal) {
				newSq = board.getSquare(i, axis);
			} else if (diagonal) {
				newSq = board.getSquare(i, i);
			} else {
				newSq = board.getSquare(axis, i);
			}
			
			if (!newSq.isEmpty()) {
				if (getColor() != newSq.getPiece().getColor())
					ok.add(newSq);
				break;
			} else {
				ok.add(newSq);
			}
		}
		return ok;
	}
	public String toString() {
		return "Queen [inPlay=" + inPlay + ", color=" + color + "]";
	}
}
