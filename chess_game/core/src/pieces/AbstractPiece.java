package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
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
	public ArrayList<Move> getLegalMoves(Square origin, IBoard board, PieceColor playerOne) {
		ArrayList<Move> legalMoves = new ArrayList<>();
		ArrayList<Move> moves = allFreeMoves(origin.getX(), origin.getY(), board, null);
		for(int i = 0; i < moves.size(); i++) {
			// TODO: 18/03/2018 bug here, sometimes getTo is null?
			if (moves.get(i) == null) {
				System.out.println("moves.get(i) null");
				continue;
			}
			Square sq = moves.get(i).getTo();
			if(sq.isEmpty()) {
				legalMoves.add(moves.get(i));
			}else if(sq.getPiece().getColor() != getColor()) {
				legalMoves.add(moves.get(i));
			}
		}
		//this line is buggy as hell (:::::::::
		// TODO: 19/03/2018 find a better solution to this method; it generates "ghost-tiles"
		moves = removeMovesThatPutYourselfInCheck(legalMoves, origin, board);
		return moves;
	}

	protected ArrayList<IPiece> enemiesReached(int x, int y, IBoard board, PieceColor opponent, ArrayList<Move> check) {
		ArrayList<IPiece> reach = new ArrayList<IPiece>();
		if (check == null) {return reach;}
		for(Move mov : check) {

			// TODO: 18/03/2018 possible bug here aswell, mov is sometimes null, why?
			if (mov == null) {
				//System.out.println("mov null");
				continue;
			}
			Square sq = mov.getTo();
			if (!sq.isEmpty())
				if (sq.getPiece().getColor() == opponent && !reach.contains(sq.getPiece()))
					reach.add(sq.getPiece());
		}
		return reach;
	}

	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<Move> check = allFreeMoves(x, y, board, null);
		return enemiesReached(x, y, board, opponent, check);
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
	 * @param legalMoves, all legal positions you can move to.
	 * @param origin, the square the piece is originally in
	 * @param board
	 * @return a updated list of positions where you can move.
	 */
	protected ArrayList<Move> removeMovesThatPutYourselfInCheck(ArrayList<Move> legalMoves, Square origin, IBoard board){
		PieceColor opponent;
		if (getColor() == PieceColor.WHITE) {opponent = PieceColor.BLACK;}
		else {opponent = PieceColor.WHITE;}
	
		ArrayList<Move> okMov = new ArrayList<Move>();
		
		for (Move m : legalMoves) {
			IBoard testBoard = board.copy();
			Square to = testBoard.getSquare(m.getTo().getX(), m.getTo().getY());
			Square from = testBoard.getSquare(m.getFrom().getX(), m.getFrom().getY());
			
			if(to.isEmpty()) {
				movePiece(from, to);
			} else {
				captureEnemyPieceAndMovePiece(from, to);
			}
			
			ArrayList<IPiece> threatened = testBoard.piecesThreatenedByOpponent(getColor(), opponent);
			if (!threatensKing(threatened)) {
				//removes illegal move
				okMov.add(m);
			}
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
	 * @param board
	 * @param playerOne, the PieceColor of first player
	 * @param x-position of rook
	 * @param y-position of rook
	 * @return list of all reachable fields in moving direction of piece
	 */
	protected abstract ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne);

	/**
	 * Method to extract moves more easily
	 * @param origin
	 * @param x
	 * @param y
	 * @param board
	 * @return Legal move, null if illegal.
	 */
	protected Move getMove(Square origin, int x, int y, IBoard board) {
		return getMove(origin, board.getSquare(x, y));
	}

	/**
	 * Method to extract moves more easily
	 * @param origin
	 * @param dest
	 * @return Legal move, null if illegal.
	 *
	 */
	protected Move getMove(Square origin, Square dest) {
		if(dest.isEmpty()) {
			return new Move(origin, dest, this, null, MoveType.REGULAR);
		} else if (dest.getPiece().getColor() != getColor()) {
			return new Move(origin, dest, this, dest.getPiece(), MoveType.REGULAR);
		}
		return null;
	}

}
