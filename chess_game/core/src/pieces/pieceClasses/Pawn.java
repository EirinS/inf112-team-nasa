package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

/**
 * This class represents a pawn in the chess game.
 * @author marianne
 *
 */
public class Pawn extends AbstractPiece {
	private boolean inPlay;
	private boolean hasMoved;
	
	/**
	 * Constructs a pawn
	 * @param color the color of the pawn
	 */
	public Pawn(PieceColor color) {
		this.color = color;
		this.inPlay = true;
		this.hasMoved = false;
	}
	
	//TODO en passant
	
	/**
	 * Gets a list of all legal moves for this pawn
	 */
	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> m = new ArrayList<>();
		Square sq = board.getSquare(x, y);
		m.addAll(reachableSquares(sq, board, playerOne));
		return m;

	}
	
	/**
	 * Checks whether this pawn has moved yet.
	 * @return whether this pawn has moved
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Builds a list of all legal moves for this pawn
	 * @param origin the square this pawn moves from
	 * @param board the board this pawn is placed on
	 * @return
	 */
	public ArrayList<Move> reachableSquares(Square origin, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		int x = origin.getX();
		int y = origin.getY();
		int dy = 0;
		PieceColor opponentColor = null;
		if (color == PieceColor.WHITE) {
			dy = -1;
			opponentColor = PieceColor.BLACK;
		} else {
			dy = 1;
			opponentColor = PieceColor.WHITE;
		}
		
		// Check whether the squares in question are legal positions
		if (y+dy >= 0) {
			// Check square straight ahead
			Square oneAhead = board.getSquare(x, y+dy);
			if (oneAhead.isEmpty()) {
				Move move = new Move(origin, oneAhead, this, null, MoveType.REGULAR);
				reachable.add(move);
			}
			// Check if this pawn can move two squares ahead
			if (y+2*dy >= 0) {
				Square twoAhead = board.getSquare(x, y+2*dy);
				if (!hasMoved && oneAhead.isEmpty() && twoAhead.isEmpty()) {
					Move move = new Move(origin, twoAhead, this, null, MoveType.REGULAR);
					reachable.add(move);
				}
			}
		}
		
		//TODO 19/03/2018 Bug: west/eastAhead.getPiece() returns null
		if (x != 0) {
			Square westAhead = board.getSquare(x-1, y+dy);
			if (westAhead.getPiece() != null && westAhead.getPiece().getColor() == opponentColor) {
				Move move = new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.REGULAR);
				reachable.add(move);
			}
		}
		if (x != board.getWidth()-1) { 
			Square eastAhead = board.getSquare(x+1, y+dy);
			if (eastAhead.getPiece() != null && eastAhead.getPiece().getColor() == opponentColor) {
				Move move = new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.REGULAR);
				reachable.add(move);
			}
		}

		// TODO: 18/03/2018 midlertidlig imens pawn ikke fynker 
		reachable.add(getMove(origin, origin.getX(), origin.getY() - 1, board));
		return reachable;
	}
	
	/**
	 * Returns a string that represents this pawn.
	 */
	@Override
	public String toString() {
		return "P";
	}
}
