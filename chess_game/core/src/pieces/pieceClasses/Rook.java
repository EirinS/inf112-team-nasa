package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Rook extends AbstractPiece {

	public Rook(PieceColor color) {
		inPlay = true;
		this.color = color;
	}

	@Override
	public ArrayList<Square> legalPositions(Square sq, IBoard board) {
		ArrayList<Square> legalPositions = new ArrayList<>();
		
		//valid moves in x-direction
		legalPositions.addAll(getValidSquares(sq.getX(), sq.getY(), board, true));
		
		//valid moves in y-direction
		legalPositions.addAll(getValidSquares(sq.getY(), sq.getX(), board, false));
		return legalPositions;
	}
	
	/**
	 * Finds all positions in a straight line from start point.
	 * @param startPoint, the point you start from.
	 * @param axis, the axis you move on. If you move on y-axis, you
	 * change the x-coordinate and find all position in x direction.
	 * @param board, the board you're working on.
	 * @return ArrayList<Square>, all legal positions on the axis.
	 */
	private ArrayList<Square> getValidSquares(int startPoint, int axis, IBoard board, boolean horizontal){
		ArrayList<Square> ok = new ArrayList<>();
		Square newSq;
		
		//check axis upwards
		for(int i = startPoint+1; i < board.getDimension(); i++) {
			
			//looking on y-axis or x-axis
			if (horizontal) {newSq= board.getSquare(i, axis);}
			else {newSq = board.getSquare(axis, i);}
			
			if(!newSq.isEmpty()) {
				break;
			}
			ok.add(newSq);
		}
		
		//check axis downwards
		for(int i = startPoint-1; i > 0; i--) {
			
			//check if we're looking on y-axis or x-axis
			if (horizontal) {newSq= board.getSquare(i, axis);}
			else {newSq = board.getSquare(axis, i);}
			
			if(!newSq.isEmpty()) {
				break;
			}
			ok.add(newSq);
		}
		return ok;			
	}

	@Override
	public String toString() {
		return "Rook [inPlay=" + inPlay + ", color=" + color + "]";
	}
}
