package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class Knight extends AbstractPiece{
	
	public Knight(PieceColor color) {
		inPlay = true;
		this.color = color;
	}

	@Override
	public ArrayList<Square> legalPositions(Square square, IBoard board) {
		ArrayList<Square> legalPositions = new ArrayList<Square>();
		ArrayList<Square> moveSquares;
		legalPositions.addAll(getMovableSquares(square.getX(), square.getY(), board));
		moveSquares = removePositionsInCheck(legalPositions, square, board);
		return moveSquares;
	}

	@Override
	public void movePiece(Square cur, Square next) {
		next.putPiece(cur.movePiece());
		
	}

	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
		reachable.addAll(reachableSquares(x, board));
		return reachable;
	
	}
	public ArrayList<Square> reachableSquares(int startPoint, IBoard board) {
		ArrayList<Square> ok = new ArrayList<Square>();
		Square newSq;
		
			newSq = board.getSquare(startPoint+1, startPoint+2);
			ok.add(newSq);
			newSq = board.getSquare(startPoint+2, startPoint+1);
			ok.add(newSq);
			
			newSq = board.getSquare(startPoint+1, startPoint-2);
			ok.add(newSq);
			newSq = board.getSquare(startPoint+2, startPoint-1);
			ok.add(newSq);
			
			newSq = board.getSquare(startPoint-1, startPoint+2);
			ok.add(newSq);
			newSq = board.getSquare(startPoint-2, startPoint+1);
			ok.add(newSq);
			
			newSq = board.getSquare(startPoint-1, startPoint-2);
			ok.add(newSq);
			newSq = board.getSquare(startPoint-2, startPoint-1);
			ok.add(newSq);
			
		return ok;
	}
	
	public String toString() {
		return "Knight [inPlay=" + inPlay + ", color=" + color + "]";
	}

}
