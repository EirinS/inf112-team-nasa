package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;

public abstract class AbstractPiece implements IPiece {
	/**
	 * Is piece on the board?
	 */
	protected boolean inPlay;	

	/**
	 * Color of piece
	 */
	protected PieceColor color;

	/**
	 * Did a piece move during this game?
	 * Relevant for the rook, pawn and king,
	 * but easier to add for all.
	 */
	protected boolean hasMoved;

	@Override
	public void pieceMoved() {
		hasMoved = true;
	}

	@Override
	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public boolean isInPlay() {
		return inPlay;		
	}

	@Override
	public PieceColor getColor() {
		return color;
	}

	@Override
	public IPiece takePiece() {
		inPlay = false;
		return this;
	}	

	@Override
	public ArrayList<Square> getEmptySquares(int x, int y, IBoard board){
		ArrayList<Square> reach = new ArrayList<>();
		for(Square sq : allReachableSquares(x, y, board)) {
			if (sq.isEmpty())
				reach.add(sq);
		}
		return reach;
	}
	
	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<IPiece> reach = new ArrayList<>();
		for(Square sq : allReachableSquares(x, y, board)) {
			if (!sq.isEmpty())
				if (sq.getPiece().getColor() == opponent)
					reach.add(sq.getPiece());	
		}
		return reach;
	}


	/**
	 * Finds and returns all fields that can be reached by piece,
	 * including first piece met.
	 * This does not check if king is left in check of you move to one
	 * of the empty reachable fields, and take into account that the
	 * list will contain the non-empty squares of the reachable
	 * pieces from this position.
	 * @param x-position of rook
	 * @param y-position of rook
	 * @param board
	 * @return list of all reachable fields in moving direction of piece
	 */
	protected abstract ArrayList<Square> allReachableSquares(int x, int y, IBoard board);
}
