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
	 * Returns 
	 * 
	 * @param sq,
	 *            square of the rook
	 * @param board,
	 *            board the rook is on
	 * @return legal castling moves, or null, if no castling move possible.
	 */
	public ArrayList<Move> castling(Square origin, IBoard board) {
		ArrayList<Move> legalCastlingMoves = new ArrayList<>();
		PieceColor opponent;
		if(getColor() == PieceColor.WHITE) {opponent = PieceColor.BLACK;}
		else { opponent = PieceColor.WHITE;}

		//conditions that make castling illegal
		if(hasMoved()) {return null;} //king moved
		if(threatensKing(board.piecesThreatenedByOpponent(getColor(), opponent))) {return null;} //king is in check
		if(getFirstPieceHorizontal(origin, board).isEmpty()) {return null;} //no pieces near king

		//castling may be possible, find rooks that can do castling
		ArrayList<Square> rookPositions = new ArrayList<>();
		for(Map.Entry<IPiece, Square> entry : getFirstPieceHorizontal(origin, board).entrySet()) {
			if (entry.getKey() instanceof Rook) {
				if(!entry.getKey().hasMoved()) //did rook  move before?
					rookPositions.add(entry.getValue());
			}
		}

		//check castling type
		if(!rookPositions.isEmpty()) {
			for(Square square : rookPositions) {
				if(square.getX() > origin.getX()) {
					if (!kingMovesThroughCheckPos(origin, board, true)){ //king never in check
						legalCastlingMoves.add(new Move(origin, square, this, null, MoveType.KINGSIDECASTLING));
					}
				} if (square.getX() < origin.getX()) {
					if (!kingMovesThroughCheckPos(origin, board, false)){ //king never in check
						legalCastlingMoves.add(new Move(origin, square, this, null, MoveType.QUEENSIDECASTLING));
					}
				}
			}
			return legalCastlingMoves;
		}
		//if castling not possible
		return null;
	}

	public boolean kingMovesThroughCheckPos(Square origin, IBoard board, boolean kingSide) {
		ArrayList<Move> kingPassesThroughPos = new ArrayList<>();
		if (kingSide) {
			kingPassesThroughPos.add(new Move (origin, board.getSquare(origin.getX()+1, origin.getY()), this, null, MoveType.REGULAR));
			kingPassesThroughPos.add(new Move (origin, board.getSquare(origin.getX()+2, origin.getY()), this, null, MoveType.REGULAR));
			if (removeMovesThatPutYourselfInCheck(kingPassesThroughPos, origin, board).size() < 2) //size 2 if no position in check
				return true;
			else 
				return false;
		}
		else {
			kingPassesThroughPos.clear();
			kingPassesThroughPos.add(new Move (origin, board.getSquare(origin.getX()-1, origin.getY()), this, null, MoveType.REGULAR));
			kingPassesThroughPos.add(new Move (origin, board.getSquare(origin.getX()-2, origin.getY()), this, null, MoveType.REGULAR));
			if (removeMovesThatPutYourselfInCheck(kingPassesThroughPos, origin, board).size() < 2) { //size 2 if no position in check
				return true;
			}
			else 
				return false;
		}
	}

	/**
	 * Method to get the first pieces in horizontal direction.
	 * Castling only legal if it is rook, and rook is in edges of board.
	 * @param sq
	 * @param board
	 * @return
	 */
	public Map<IPiece, Square> getFirstPieceHorizontal(Square sq, IBoard board) {
		Map<IPiece, Square> pieces = new HashMap<IPiece, Square>();
		int x = sq.getX(), y = sq.getY();
		for(int i = x-1; i >= 0; i--) {
			if (!board.getSquare(i, y).isEmpty()) {
				pieces.put(board.getSquare(i, y).getPiece(), board.getSquare(i, y));
				break;
			}
		}

		for(int i = x+1; i < board.getWidth(); i++) {
			if (!board.getSquare(i, y).isEmpty()) {
				pieces.put(board.getSquare(i, y).getPiece(), board.getSquare(i, y));
				break;
			}
		}
		return pieces;
	}

	/**
	 * All reachable squares from king.
	 * @param x
	 * @param y
	 * @param origin
	 * @param board
	 * @return
	 */
	public ArrayList<Move> reachable(int x, int y, Square origin, IBoard board){
		ArrayList<Move> lst = new ArrayList<>();
		Move mv;
		if(board.withinBoard(x+1, y)) {
			mv = getMove(origin, (x+1), y, board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		if(board.withinBoard(x+1, y-1)) {
			mv = getMove(origin, (x+1), (y-1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}

		if(board.withinBoard(x+1, y+1)) {
			mv = getMove(origin, (x+1), (y+1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}

		if(board.withinBoard(x, y-1)) {
			mv = getMove(origin, x, (y-1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		if(board.withinBoard(x, y+1)) {
			mv = getMove(origin, x, (y+1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		if(board.withinBoard(x-1, y)) {
			mv = getMove(origin, (x-1), (y), board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		if(board.withinBoard(x-1, y+1)) {
			mv = getMove(origin, (x-1), (y+1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		if(board.withinBoard(x-1, y-1)) {
			mv = getMove(origin, (x-1), (y-1), board);
			if (mv != null) {
				lst.add(mv);
			}
		}
		return lst;		
	}

	@Override
	public String toString() {
		return "K";
	}
}
