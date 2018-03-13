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
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		// TODO Auto-generated method stub
		return reachable(x,y,board);
	}
	
	/**
	 * NOT IMPLEMENTED
	 * 
	 * @param sq,
	 *            square of the rook
	 * @param board,
	 *            board the rook is on
	 * @return legal castling square moves.
	 */
	private ArrayList<Square> castling(Square sq, IBoard board) {
		// TODO
		/*
		 * CONDITIONS FOR THIS MOVE: - Neither the king, nor rook has moved before - No
		 * pieces between king and chosen rook - The king never passes through pieces
		 * where it's in check - King is not in check
		 */
		return null;
	}
	
	public ArrayList<Square> reachable(int x, int y, IBoard board){
		ArrayList<Square> lst = new ArrayList<>();	
		if(board.withinBoard(x+1, y)) {
			lst.add(board.getSquare(x+1, y));
		}
		if(board.withinBoard(x+1, y-1)) {
			lst.add(board.getSquare(x+1, y-1));
		}
		if(board.withinBoard(x+1, y+1)) {
			lst.add(board.getSquare(x+1, y+1));
		}
		if(board.withinBoard(x, y-1)) {
			lst.add(board.getSquare(x, y-1));
		}
		if(board.withinBoard(x, y+1)) {
			lst.add(board.getSquare(x, y+1));
		}
		if(board.withinBoard(x-1, y)) {
			lst.add(board.getSquare(x-1, y));
		}
		if(board.withinBoard(x-1, y+1)) {
			lst.add(board.getSquare(x-1, y+1));
		}
		if(board.withinBoard(x-1, y-1)) {
			lst.add(board.getSquare(x-1, y-1));
		}
		return lst;		
		
	}

}
