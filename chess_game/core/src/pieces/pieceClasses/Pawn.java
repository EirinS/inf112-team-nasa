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
	private PieceColor color;
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
	
	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(allFreeMoves(x, y, board));
		return reachable;
	}
	
	/**
	 * Checks whether this pawn has moved yet.
	 * @return whether this pawn has moved
	 */
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public ArrayList<Move> reachableSquares(Square origin, IBoard board) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		int x = origin.getX();
		int y = origin.getY();
		Square oneAhead = null;
		Square twoAhead = null;
		Square westAhead = null; // One square ahead and westward
		Square eastAhead = null; // One square ahead and eastward
		PieceColor opponentColor = null;
		
		// Check whether the squares in question are legal positions
		if (color == PieceColor.WHITE) {
			if (y-1 < 0) oneAhead = new Square(x, y-1);
			if (y-2 < 0) twoAhead = new Square(x, y-2);
			if (x != 0) westAhead = new Square(x-1, y-1);
			if (x != board.getWidth()-1) eastAhead = new Square(x+1, y-1);
			opponentColor = PieceColor.BLACK;
		} else {
			if (y+1 > board.getHeight()-1) oneAhead = new Square(x, y+1);
			if (y+2 > board.getHeight()-1) twoAhead = new Square(x, y+2);
			if (x != 0) westAhead = new Square(x-1, y+1);
			if (x != board.getWidth()-1) eastAhead = new Square(x+1, y+1);
			opponentColor = PieceColor.WHITE;
		}
		
		// Check square straight ahead
		if (oneAhead != null && oneAhead.isEmpty()) {
			Move move = new Move(origin, oneAhead, this, null, MoveType.REGULAR);
			reachable.add(move);
		}
		
		// Check if this pawn can move two squares ahead
		if (!hasMoved && oneAhead.isEmpty() && twoAhead.isEmpty()) {
			Move move = new Move(origin, twoAhead, this, null, MoveType.REGULAR);
			reachable.add(move);
		}

		// Check for opponent pieces on both sides of the square straight ahead
		if (westAhead != null && westAhead.getPiece() != null
				&& westAhead.getPiece().getColor() == opponentColor) {
			Move move = new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.REGULAR);
			reachable.add(move);
		}
		if (eastAhead != null && eastAhead.getPiece() != null
				&& eastAhead.getPiece().getColor() == opponentColor) {
			Move move = new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.REGULAR);
			reachable.add(move);
		}

		return reachable;
	}
	
	/**
	 * Returns a string that represents this pawn.
	 */
	public String toString() {
		return "Pawn [inPlay=" + inPlay + ", color=" + color + "]";
	}
}
