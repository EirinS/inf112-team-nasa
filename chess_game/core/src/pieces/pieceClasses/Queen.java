package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
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
		reachable.addAll(reachableSquaresStraight(x, y, origin, board, true));
		reachable.addAll(reachableSquaresStraight(y, x, origin, board, false));
		reachable.addAll(reachableSquaresDiagonalBottomLeftToTopRight(x, y, origin, board));
		reachable.addAll(reachableSquaresDiagonalBottomRightToTopLeft(x, y, origin, board));
		return reachable;
	}

	/**
	 * Reachable squares in a straight line from Queen.
	 * @param startPoint
	 * @param axis
	 * @param origin
	 * @param board
	 * @param horizontal
	 * @return
	 */
	public ArrayList<Move> reachableSquaresStraight(int startPoint, int axis, Square origin, IBoard board, boolean horizontal) {
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

	/**
	 * Gets all the moves from 
	 * @param startPoint
	 * @param axis
	 * @param origin
	 * @param board
	 * @return
	 */
	public ArrayList<Move>reachableSquaresDiagonalBottomLeftToTopRight(int x, int y, Square origin, IBoard board) {
		ArrayList<Move> ok = new ArrayList<>();
		int i = x-1; int j = y+1;
		Square dest = null;

		if(board.withinBoard(i,j)) {
			while (i >= 0 && j < board.getHeight()) {
				dest = board.getSquare(i, j);
				if(!dest.isEmpty()) {
					if (dest.getPiece().getColor() != getColor()) {
						ok.add(getMove(origin, dest));
					}
					break;
				} else if(dest.isEmpty()) {
					ok.add(getMove(origin, dest));
				}
				i--; j++;
			}
		}


		i = x+1; j = y-1;
		if(board.withinBoard(i,j)) {
			while (i < board.getWidth() && j >= 0) {
				dest = board.getSquare(i, j);
				if(!dest.isEmpty()) {
					if (dest.getPiece().getColor() != getColor()) {
						ok.add(getMove(origin, dest));
					}
					break; 
				} else if(dest.isEmpty()) {
					ok.add(getMove(origin, dest));
				}
				i++; j--;
			}
		}
		return ok;
	}

	/**
	 * Get
	 * @param startPoint
	 * @param axis
	 * @param origin
	 * @param board
	 * @return the possible moves on this diagonal
	 */
	public ArrayList<Move>reachableSquaresDiagonalBottomRightToTopLeft(int x, int y, Square origin, IBoard board) {
		ArrayList<Move> ok = new ArrayList<>();
		int i = x+1; int j = y+1;
		Square dest = null;

		if(board.withinBoard(i, j)) {
			while (i < board.getWidth() && j < board.getHeight()) {
				dest = board.getSquare(i, j);
				if(!dest.isEmpty()) {
					if (dest.getPiece().getColor() != getColor()) {
						ok.add(getMove(origin, dest));
					}
					break;
				}
				if(dest.isEmpty()) {
					ok.add(getMove(origin, dest));
				}
				i++; j++;
			}
		}

		i = x-1; j = y-1;
		if(board.withinBoard(i,j)) {
			while (i >= 0 && j >= 0) {
				dest = board.getSquare(i, j);
				if(!dest.isEmpty()) {
					if (dest.getPiece().getColor() != getColor()) {
						ok.add(getMove(origin, dest));
					}
					break;
				}
				if(dest.isEmpty()) {
					ok.add(getMove(origin, dest));
				}
				i--; j--;
			}
		}
		return ok;
	}
	
	@Override
	public IPiece copy() {
		Queen q = new Queen(this.getColor());
		if (this.hasMoved())
			q.pieceMoved();
		return q;
	}

	@Override
	public String toString() {
		return "Q";
	}
}
