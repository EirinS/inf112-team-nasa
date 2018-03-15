package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Rook extends AbstractPiece {

	public Rook(PieceColor color) {
		inPlay = true;
		this.color = color;
		hasMoved = false;
	}


	@Override
	public ArrayList<Move> allFreeMoves(int x, int y, IBoard board) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(reachableSquares(x, y, board.getSquare(x, y), board, true));
		reachable.addAll(reachableSquares(y, x, board.getSquare(x, y), board, false));
		return reachable;
	}

	/**
	 * NOT IMPLEMENTED
	 * 
	 * @param sq,
	 *            square of the rook
	 * @param board,
	 *            board the rook is on
	 * @return legal castling square moves.
	 */
	private ArrayList<Move> castling(Square sq, IBoard board) {
		// TODO
		/*
		 * CONDITIONS FOR THIS MOVE: - Neither the king, nor rook has moved before - No
		 * pieces between king and chosen rook - The king never passes through pieces
		 * where it's in check - King is not in check
		 */
		return null;
	}

	/**
	 * Finds all positions in a straight line from start point, including first
	 * piece encountered.
	 * 
	 * @param startPoint,
	 *            the point you start from.
	 * @param axis,
	 *            the axis you move on. If you move on y-axis, you change the
	 *            x-coordinate and find all position in x direction.
	 * @param board,
	 *            the board you're working on.
	 * @param horizontal,
	 *            tells you if you're checking horizontal or vertical direction.
	 * @return ArrayList<Square>, all legal positions on the axis.
	 */
	public ArrayList<Move> reachableSquares(int startPoint, int axis, Square origin, IBoard board, boolean horizontal) {
		ArrayList<Move> ok = new ArrayList<>();
		Square dest;

		// check axis upwards
		for (int i = startPoint + 1; i < board.getDimension(); i++) {

			// looking on y-axis or x-axis
			if (horizontal) {
				dest = board.getSquare(i, axis);
			} else {
				dest = board.getSquare(axis, i);
			}

			if (!dest.isEmpty()) {
				if (getColor() != dest.getPiece().getColor())
					ok.add(getMove(origin, dest, board));
				break;
			} else {
				ok.add(getMove(origin, dest, board));
			}
		}

		// check axis downwards
		for (int i = startPoint - 1; i >= 0; i--) {

			// check if we're looking on y-axis or x-axis
			if (horizontal) {
				dest = board.getSquare(i, axis);
			} else {
				dest = board.getSquare(axis, i);
			}

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

	@Override
	public String toString() {
		return "R";
	}

}
