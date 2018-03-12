package pieces;

import java.util.ArrayList;

import boardstructure.IBoard;
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
	public PieceColor getColor() {
		return color;
	}

	@Override
	public IPiece takePiece() {
		inPlay = false;
		return this;
	}	
	

	@Override
	//note to self, må endres når man implementerer å ta brikker, sannsynligvis legge til ta-brikke-logikk
	public ArrayList<Square> getMovableSquares(int x, int y, IBoard board){
		ArrayList<Square> reach = new ArrayList<Square>();
		ArrayList<Square> check = allReachableSquares(x, y, board);
		for(Square sq : check) {
			if (sq.isEmpty())
				reach.add(sq);
		}
		return reach;
	}
	
	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<IPiece> reach = new ArrayList<IPiece>();
		ArrayList<Square> check = allReachableSquares(x, y, board);
		if (check == null) {return reach;}
		for(Square sq : check) {
			if (!sq.isEmpty())
				if (sq.getPiece().getColor() == opponent)
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
	protected ArrayList<Square> removePositionsInCheck(ArrayList<Square> legalPositions, Square origin, IBoard board){
		PieceColor opponent;
		if (getColor() == PieceColor.WHITE) {opponent = PieceColor.BLACK;}
		else {opponent = PieceColor.WHITE;};
		ArrayList<Square> okPos = new ArrayList<Square>();
		for(Square movSq : legalPositions) {	
			//temporary move
			movePiece(origin, movSq);
			ArrayList<IPiece> threatened = board.piecesThreatenedByOpponent(getColor(), opponent);
			//reverts move
			movePiece(movSq, origin);
			if (!threatensKing(threatened)) {
				//removes illegal move
				okPos.add(movSq);
			}
		}
		return okPos;
	}
	
	@Override
	public ArrayList<Square> legalPositions(Square sq, IBoard board) {
		ArrayList<Square> legalPositions = new ArrayList<Square>();
		ArrayList<Square> moveSquares;
		legalPositions.addAll(getMovableSquares(sq.getX(), sq.getY(), board));
		moveSquares = removePositionsInCheck(legalPositions, sq, board);
		return moveSquares;
	}
	
	@Override
	public void movePiece(Square cur, Square next) {
		next.putPiece(cur.movePiece());
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
	protected abstract ArrayList<Square> allReachableSquares(int x, int y, IBoard board);
}
