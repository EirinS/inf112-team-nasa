package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
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
	private boolean reachedOtherSide; //may be redundant
	
	/**
	 * Constructs a pawn
	 * @param color the color of the pawn
	 */
	public Pawn(PieceColor color) {
		this.color = color;
		this.inPlay = true;
		this.hasMoved = false;
		this.reachedOtherSide = false;
	}
	
	
	//TODO en passant
	
	
	/**
	 * 
	 */
	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
		reachable.addAll(reachableSquares(x, y, board));
		return reachable;
	}
	
	public ArrayList<Square> reachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
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
		if (oneAhead != null && oneAhead.isEmpty()) reachable.add(oneAhead);
		
		// Check if this pawn can move two squares ahead
		if (!hasMoved && oneAhead.isEmpty() && twoAhead.isEmpty()) reachable.add(twoAhead);

		// Check for opponent pieces on both sides of the square straight ahead
		if (westAhead != null && westAhead.getPiece() != null
				&& westAhead.getPiece().getColor() == opponentColor) reachable.add(westAhead);
		if (eastAhead != null && eastAhead.getPiece() != null
				&& eastAhead.getPiece().getColor() == opponentColor) reachable.add(eastAhead);

		return reachable;
	}
	
	/**
	 * Checks whether this pawn has moved yet.
	 * @return whether this pawn has moved
	 */
	public boolean hasMoved() {
		return hasMoved;
	}
	
	/**
	 * 
	 * @param square
	 * @return
	 */
	public boolean isLegalMove(Square square) {
		//TODO
		return true;
	}
	
	@Override
	public void movePiece(Square cur, Square next) {
		next.putPiece(cur.movePiece());
	}
	
	/**
	 * 
	 * @return whether the pawn has reached the far side of the board
	 */
	public boolean reachedOtherSide() {
		return reachedOtherSide;
	}
	
	/**
	 * Returns a string that represents this pawn.
	 */
	public String toString() {
		return "Pawn [inPlay=" + inPlay + ", color=" + color + "]";
	}
}
