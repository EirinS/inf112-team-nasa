package pieces.pieceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	public ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> moves = reachable(x,y, board.getSquare(x, y), board);
		if(castling(board.getSquare(x, y), board) == null) {
			return moves;
		}
		moves.addAll(castling(board.getSquare(x, y), board));
		return moves;
	}
	
	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<Move> check = reachable(x, y, board.getSquare(x, y), board);
		return enemiesReached(x, y, board, opponent, check);
	}

	/**
	 * Returns 
	 * 
	 * @param origin,
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
					//if (!kingMovesThroughCheckPos(origin, board, true)){ //king never in check
						legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()+2, origin.getY()), this, null, MoveType.KINGSIDECASTLING));
					//}
				} if (square.getX() < origin.getX()) {
					//if (!kingMovesThroughCheckPos(origin, board, false)){ //king never in check
						legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()-2, origin.getY()), this, null, MoveType.QUEENSIDECASTLING));
				//	}
				}
			}
			return legalCastlingMoves;
		}
		//if castling not possible
		return null;
	}

	/**
	 * Check if king at any point in castling, moves through a position that is in check.
	 * @param origin
	 * @param board
	 * @param kingSide
	 * @return
	 */
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
	
	/** Precondition: All positions that are moved to are empty and possible to move to.
	 * Castling is an OK move.
	 */
	public void moveCastling(Square origin, Square next, MoveType type, IBoard board) {
		//moves king
		next.putPiece(origin.movePiece());
		if (type == MoveType.KINGSIDECASTLING) {
			//moves rook
			Square rooksq = board.getSquare(board.getWidth()-1, origin.getY());
			board.getSquare(rooksq.getX()-2, rooksq.getY()).putPiece(rooksq.movePiece());
		} else if (type == MoveType.QUEENSIDECASTLING){
			//moves rook
			Square rooksq = board.getSquare(0, origin.getY());
			board.getSquare(rooksq.getX()+3, rooksq.getY()).putPiece(rooksq.movePiece());
		} else {
			throw new IllegalArgumentException("MoveType is wrong! Must be MoveType.KINGSIDECASTLING, or MoveType.QUEENSIDECASTLING");
		}
	}

	@Override
	public String toString() {
		return "K";
	}
}
