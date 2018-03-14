package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.pieceClasses.King;

public abstract class AbstractPiece implements IPiece {
	/**
	 * Is piece on the board?
	 */
	protected boolean inPlay;	

	/**
	 * Color of piece
	 */
	protected PieceColor color;

	/**
	 * Did a piece move during this game?
	 * Relevant for the rook, pawn and king,
	 * but easier to add for all.
	 */
	protected boolean hasMoved;

	@Override
	public void pieceMoved() {
		hasMoved = true;
	}

	@Override
	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public boolean isInPlay() {
		return inPlay;		
	}
	
	@Override
	public void putInPlay() {
		inPlay = true;
	}

	@Override
	public PieceColor getColor() {
		return color;
	}

	@Override
	public IPiece takePiece() {
		inPlay = false;
		return this;
	}
	
	@Override
	public void setMovedFalse() {
		hasMoved = false;
	}

	@Override
	public ArrayList<Move> getLegalMoves(Square origin, IBoard board) {
		ArrayList<Move> legalMoves = new ArrayList<>();
		ArrayList<Move> moves = allFreeMoves(origin.getX(), origin.getY(), board);
		for(int i = 0; i < moves.size(); i++) {
			Square sq = moves.get(i).getTo();
			if(sq.isEmpty()) {
				legalMoves.add(moves.get(i));
			}else if(sq.getPiece().getColor() != getColor()) {
				legalMoves.add(moves.get(i));
			}
		}
		moves = removeMovesThatPutYourselfInCheck(legalMoves, origin, board);
		return moves;
	}

	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<IPiece> reach = new ArrayList<IPiece>();
		ArrayList<Move> check = allFreeMoves(x, y, board);
		if (check == null) {return reach;}
		for(Move mov : check) {
			Square sq = mov.getTo();
			if (!sq.isEmpty())
				if (sq.getPiece().getColor() == opponent && !reach.contains(sq.getPiece()))
					reach.add(sq.getPiece());
		}
		return reach;
	}


	/**
	 * Precondition: input does not contain any of your own pieces
	 * Method to check if a list of pieces contain a king.
	 * @param threatened, all the fields that are threatened by the opponent
	 * @return true if king is threatened (will be put in check), false if not.
	 */

	public boolean threatensKing(ArrayList<IPiece> threatened) {
		for(IPiece piece : threatened) {
			if (piece instanceof King)
				return true;
		}
		return false;
	}

	/**
	 * Checks for every position that a piece can move to, and makes sure that no 
	 * open space will result in a position where your own king is in check.
	 * Does not actually move any pieces (always moves back)
	 * @param legalPositions, all legal positions you can move to.
	 * @param origin, the square the piece is originally in
	 * @param board
	 * @return a updated list of positions where you can move.
	 */
	protected ArrayList<Move> removeMovesThatPutYourselfInCheck(ArrayList<Move> legalMoves, Square origin, IBoard board){
		PieceColor opponent;
		if (getColor() == PieceColor.WHITE) {opponent = PieceColor.BLACK;}
		else {opponent = PieceColor.WHITE;}
		
		boolean hasMoved = false;
		if(origin.getPiece().hasMoved())
			hasMoved = true;
		
		IPiece p = null;
		ArrayList<Move> okMov = new ArrayList<Move>();
		for(Move move : legalMoves) {
			Square movSq = move.getTo();
			//temporary move
			if (movSq.isEmpty()) {
				movePiece(origin, movSq);	
			} else {				
				p = captureEnemyPieceAndMovePiece(origin, movSq);
			}
			
			ArrayList<IPiece> threatened = board.piecesThreatenedByOpponent(getColor(), opponent);
			//reverts move
			if(p != null) {
				revertMove(origin, movSq, p);
			} else {
				movePiece(movSq, origin);
			}
			
			if (!threatensKing(threatened)) {
				//removes illegal move
				okMov.add(move);
			}
		}
		
		//reset field variable
		if(!hasMoved) {
			origin.getPiece().setMovedFalse();
		}
		return okMov;
	}

	@Override
	public IPiece captureEnemyPieceAndMovePiece(Square origin, Square next) {
		IPiece captured = next.getPiece();
		next.takePiece();
		movePiece(origin, next);
		return captured;
	}
	
	/**
	 * Reverts a move, and puts taken piece back in place.
	 * Resets the inPlay field variable of the taken piece
	 * @param origin, the position moved from
	 * @param movedTo, the position moved to
	 * @param taken, the piece that was captured, but is put back
	 */
	protected void revertMove(Square origin, Square movedTo, IPiece taken) {
		movePiece(movedTo, origin);
		if(taken != null) {
			movedTo.putPiece(taken);
			taken.putInPlay();
		}
	}


	@Override
	public void movePiece(Square origin, Square next) {
		if(next.isEmpty()){
			next.putPiece(origin.movePiece());
		} else 
			throw new IllegalArgumentException("Only try to move to empty positions, please.");
	}


	/**
	 * Finds and returns all fields that can be reached by piece,
	 * including first piece met.
	 * This does not check if king is left in check of you move to one
	 * of the empty reachable fields, and take into account that the
	 * list will contain the non-empty squares of the reachable
	 * pieces from this position.
	 * @param x-position of rook
	 * @param y-position of rook
	 * @param board
	 * @return list of all reachable fields in moving direction of piece
	 */
	protected abstract ArrayList<Move> allFreeMoves(int x, int y, IBoard board);
}
