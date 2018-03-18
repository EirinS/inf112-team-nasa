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
	public ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(reachableSquares(x, y, board.getSquare(x, y), board, true));
		reachable.addAll(reachableSquares(y, x, board.getSquare(x, y), board, false));
		if(castling(board.getSquare(x, y), board) != null) {
			reachable.add(castling(board.getSquare(x, y), board));
		}
		return reachable;
	}

	/**
	 * Uses the method for finding castling-moves in king.
	 * @param sq
	 * @param board
	 * @return
	 */
	private Move castling(Square sq, IBoard board) {
		if (hasMoved()) {return null;}
		
		//4 is king position in regular chess, if no piece there, no castling
		if(board.getSquare(sq.getY(), 4).isEmpty()) {return null;}
		
		//if piece is not king, no castling
		if(!(board.getSquare(sq.getY(), 4).getPiece() instanceof King)) {return null;}
		
		//okay, we know king is instanceof King
		Square kingSq = board.getSquare(sq.getY(), 4);
		King k = (King) kingSq.getPiece();
		
		//no castling if king has moved.
		if(k.hasMoved()) {return null;}
		
		//check which castling-type
		boolean kingSide = false;
		if(sq.getX() == 7) {kingSide = true;}
		
		//if king moves through positions in check, no castling.
		if(k.kingMovesThroughCheckPos(kingSq, board, kingSide)) {return null;}
		
		//castling should now be possible
		if (kingSide)
			return new Move(kingSq, board.getSquare(kingSq.getX()+2, sq.getY()), k, null, MoveType.KINGSIDECASTLING);
		else 
			return new Move(kingSq, board.getSquare(kingSq.getX()-2, sq.getY()), k, null, MoveType.QUEENSIDECASTLING);
		

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
