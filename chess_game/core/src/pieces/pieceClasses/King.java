package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

public class King extends AbstractPiece {

	public King(PieceColor color) {
		this.color = color;
	}

	@Override
	public ArrayList<Square> legalPositions(Square square, IBoard board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void movePiece(Square cur, Square next) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		// TODO Auto-generated method stub
		return null;
	}

}
