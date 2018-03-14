package pieces.pieceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;

public class King extends AbstractPiece {

	public King(PieceColor color) {
		this.color = color;
	}

	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board) {
		// TODO Auto-generated method stub
		return reachable(x,y, board.getSquare(x, y), board);
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
	private ArrayList<Move> castling(Square sq, IBoard board) {
		ArrayList<Square> legalCastlingMoves = new ArrayList<>();
		PieceColor opponent;
		if(getColor() == PieceColor.WHITE) {opponent = PieceColor.BLACK;}
		else { opponent = PieceColor.WHITE;}

		if(hasMoved());
		if(threatensKing(board.piecesThreatenedByOpponent(getColor(), opponent)));
		if(getFirstPieceHorizontal(sq, board).isEmpty());


		ArrayList<Square> rookPositions = new ArrayList<>();
		for(Map.Entry<IPiece, Square> entry : getFirstPieceHorizontal(sq, board).entrySet()) {
			if (entry.getKey() instanceof Rook) {
				if(!entry.getKey().hasMoved())
					rookPositions.add(entry.getValue());
			}
		}
		if(!rookPositions.isEmpty()) {
			for(Square square : rookPositions) {
				if(square.getX() < sq.getX()) {
				}
			}
		}

		// TODO
		/*
		 * CONDITIONS FOR THIS MOVE:
		 *  - Neither the king, nor rook has moved before 
		 *  - No pieces between king and chosen rook 
		 *  - The king never passes through pieces where it's in check 
		 *  - King is not in check
		 */
		return null;
	}

	/**
	 * Method to get all 
	 * @param sq
	 * @param board
	 * @return
	 */
	private Map<IPiece, Square> getFirstPieceHorizontal(Square sq, IBoard board) {
		Map<IPiece, Square> pieces = new HashMap<IPiece, Square>();
		int x = sq.getX(), y = sq.getY();
		for(int i = 0; i < x; i++) {
			if (!board.getSquare(i, y).isEmpty()) {
				pieces.put(board.getSquare(i, y).getPiece(), board.getSquare(i, y));
				break;
			}
		}

		for(int i = x+1; i < 0; i--) {
			if (!board.getSquare(i, y).isEmpty()) {
				pieces.put(board.getSquare(i, y).getPiece(), board.getSquare(i, y));
				break;
			}
		}
		return pieces;
	}

	public ArrayList<Move> reachable(int x, int y, Square origin, IBoard board){
		ArrayList<Move> lst = new ArrayList<>();
		int newX, newY;
		if(board.withinBoard(x+1, y)) {
			newX = x+1; newY = y;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x+1, y-1)) {
			newX = x+1; newY = y-1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x+1, y+1)) {
			newX = x+1; newY = y+1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		
		if(board.withinBoard(x, y-1)) {
			newX = x; newY = y-1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x, y+1)) {
			newX = x; newY = y+1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x-1, y)) {
			newX = x-1; newY = y;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x-1, y+1)) {
			newX = x-1; newY = y+1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		if(board.withinBoard(x-1, y-1)) {
			newX = x-1; newY = y-1;
			if(board.getSquare(newX, newY).isEmpty())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, null, MoveType.REGULAR));
			else if (board.getSquare(newX, newY).getPiece().getColor() != getColor())
				lst.add(new Move(origin, board.getSquare(newX, newY), this, board.getSquare(newX, newY).getPiece(), MoveType.REGULAR));
		}
		return lst;		

	}

}
