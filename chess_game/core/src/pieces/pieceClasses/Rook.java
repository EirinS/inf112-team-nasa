package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
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
		//reachable.add(castling(board.getSquare(x, y), board));
		return reachable;
	}

	/**
	 * Finds a castling move, if there is one, for this rook.
	 * @param sq, the square this rook is in
	 * @param board
	 * @return Move m, that is the castling move, if there is one, null if not.
	 */
	public Move castling(Square sq, IBoard board) {
		//no castling if piece moved
		if (hasMoved()) {return null;}
		
		Square kingSq = board.getKingPos(getColor());
		
		//no king, no castling.
		if(kingSq == null) {return null;}
		King k = (King) kingSq.getPiece();
		
		//king has moved, no castling.
		if (k.hasMoved()) {return null;}		
		
		//check which castling-type
		boolean kingSide = false;
		if(sq.getX() == 7) {kingSide = true;}
		
		//if king moves through positions in check, no castling.
		//if(k.kingMovesThroughCheckPos(kingSq, board, kingSide)) {return null;}
		
		//this rook is not on a free path to king
		if(!k.getFirstPieceHorizontal(kingSq, board).containsKey(this)) {return null;}
		
		//castling should now be possible
		if (kingSide)
			return new Move(sq, board.getSquare(sq.getX()-2, sq.getY()), this, null, MoveType.KINGSIDECASTLING);
		else 
			return new Move(sq, board.getSquare(sq.getX()+3, sq.getY()), this, null, MoveType.QUEENSIDECASTLING);
	}
	
	/**
	 *  Precondition: All positions that are moved to are empty and possible to move to.
	 * Castling is an OK move.
	 * @param origin
	 * @param next
	 * @param type
	 * @param board
	 */
	public void moveCastling(Square origin, Square next, MoveType type, IBoard board) {
		//moves rook
		next.putPiece(origin.movePiece());
		
		Square kingSq = board.getKingPos(getColor());
		if (type == MoveType.KINGSIDECASTLING) {
			board.getSquare(kingSq.getX()+2, kingSq.getY()).putPiece(kingSq.movePiece());
		} else if (type == MoveType.QUEENSIDECASTLING){
			//moves rook
			board.getSquare(kingSq.getX()-2, kingSq.getY()).putPiece(kingSq.movePiece());
		} else {
			throw new IllegalArgumentException("MoveType is wrong! Must be MoveType.KINGSIDECASTLING, or MoveType.QUEENSIDECASTLING");
		}
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
					ok.add(getMove(origin, dest));
				break;
			} else {
				ok.add(getMove(origin, dest));
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
					ok.add(getMove(origin, dest));
				break;
			} else {
				ok.add(getMove(origin, dest));
			}
		}		
		return ok;
	}

	@Override
	public String toString() {
		return "R";
	}


	@Override
	public IPiece copy() {
		Rook r = new Rook(this.getColor());
		if (this.hasMoved())
			r.pieceMoved();
		return r;
	}
}
