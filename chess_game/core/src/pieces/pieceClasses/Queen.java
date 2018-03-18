package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Queen extends AbstractPiece {
	
	public Queen(PieceColor color) {
		inPlay = true;
		this.color = color;
	}

	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		Square origin = board.getSquare(x, y);
		reachable.addAll(reachableSquares(x, y, origin, board, true, false));
		reachable.addAll(reachableSquares(y, x, origin, board, false, true));
		reachable.addAll(reachableSquares(x, y, origin, board, false, false));
		return reachable;
		
	}

	public ArrayList<Move> reachableSquares(int startPoint, int axis, Square origin, IBoard board, boolean horizontal,
			boolean diagonal) {
		ArrayList<Move> ok = new ArrayList<Move>();

		for (int i = startPoint + 1; i < board.getDimension(); i++) {
			Square dest = getSquare(axis, board, horizontal, diagonal, i);

			if (!dest.isEmpty()) {
				if (getColor() != dest.getPiece().getColor())
					ok.add(getMove(origin, dest, board));
				break;
			} else {
				ok.add(getMove(origin, dest, board));
			}
		}
		
		for (int i = startPoint - 1; i >= 0; i--) {
			Square dest = getSquare(axis, board, horizontal, diagonal, i);

			if (!dest.isEmpty()) {
				if (getColor() != dest.getPiece().getColor())
					ok.add(getMove(origin, dest, board));
				break;
			} else {
				ok.add(getMove(origin, dest, board));
			}
		}
		return ok;
	}

	private Square getSquare(int axis, IBoard board, boolean horizontal, boolean diagonal, int i) {
		Square newSq;
		if (horizontal) {
            newSq = board.getSquare(i, axis);
        } else if (diagonal) {
            newSq = board.getSquare(i, i);
        } else {
            newSq = board.getSquare(axis, i);
        }
		return newSq;
	}

	public String toString() {
		return "Q";
	}
}
