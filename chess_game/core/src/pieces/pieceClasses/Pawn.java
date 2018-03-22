package pieces.pieceClasses;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;

import static pieces.PieceColor.WHITE;
import static pieces.PieceColor.BLACK;


/**
 * This class represents a pawn in the chess game.
 *
 * @author marianne
 */
public class Pawn extends AbstractPiece {

	/**
	 * Constructs a pawn
	 *
	 * @param color the color of the pawn
	 */
	public Pawn(PieceColor color) {
		this.color = color;
		this.hasMoved = false;
		this.inPlay = true;
	}

	//TODO en passant
	
	/**
	 * Gets a list of all legal moves for this pawn
	 */
	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> m = new ArrayList<>();
		Square sq = board.getSquare(x, y);
		m.addAll(reachableSquares(sq, board, playerOne));
		return m;
	}
	
	/**
	 * Returns the piece captured during en passant
	 * @param dir the square the captured piece just passed through
	 * @return the piece captured en passant
	 */
	private IPiece capturedEnPassant(Square dir, int dy, IBoard board) {
		int x = dir.getX(); 
		int y = dir.getY();
		if (dy == -1)
			return board.getSquare(x, y + 1).getPiece();
		else
			return board.getSquare(x, y - 1).getPiece();
	}
	
	/**
	 * Checks whether this pawn can capture another pawn en passant.
	 * @param x the x coordinate of this pawn
	 * @param y the y coordinate of this pawn
	 * @param dy the difference in y direction between origin and destination
	 * @param board the board this pawn is placed upon
	 * @return whether en passant is possible now
	 */
	private boolean enPassantIsValid(PieceColor playerOne, int x, int y, int dy, IBoard board) {
		//TODO 21/03 Bug: getLastMove returns null the majority of the time?
//		Move p = board.getLastMove();
//		System.out.println(p);
		
		// Get previous move
		ArrayList<Move> moves = board.getHistory();
		if (moves.size() == 0) return false;
		Move previous = moves.get(moves.size() - 1);
		
		//TODO 22/03 Bug: Moves where pawns captures another pawn are sometimes not registered in moves?
		
		int toX = previous.getTo().getY();
		int toY = previous.getTo().getY();
		
		// Check whether previous move was a two-step pawn move 
		char[] move = previous.toString().toCharArray();
		if (move[0] != 'P' || move[1] != move[3] || move.length > 5
				|| (Math.max(move[2], move[4]) - Math.min(move[2], move[4])) != 2)
			return false;
			
//		System.out.println("Player one's turn: " +(board.getTurn() == playerOne));
//		System.out.println("y==3: "+(y == 3));
//		System.out.println("y==4: "+(y == 4));
//		System.out.println("toY==y: "+(toY == y));
//		System.out.println("Diff x ==1: "+(Math.max(x, toX) - Math.min(x, toX) == 1));
		
		//TODO board.getTurn is always player one for some reason?
		
		return ((board.getTurn() == playerOne && y == 3 
				&& toY == y && Math.max(x, toX) - Math.min(x, toX) == 1)
				|| (board.getTurn() == playerOne.getOpposite() 
				&& y == 4 && toY == y && Math.max(x, toX) - Math.min(x, toX) == 1));
//		return false;
	}

	/**
	 * Checks whether this pawn has moved yet.
	 *
	 * @return whether this pawn has moved
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Checks whether this pawn has reached the other end of the board.
	 * @param y the y coordinate of this pawn
	 * @param dy the difference in y direction between origin and destination
	 * @param board the board this pawn is placed upon
	 * @return whether pawn promotion should be triggered now
	 */
	private boolean pawnPromotionIsValid(int y, int dy, IBoard board) {
		return (dy == -1 && (y + dy) == 0) || (dy == 1 && (y + dy) == (board.getHeight() - 1));
	}

	/**
	 * Builds a list of all legal moves for this pawn
	 * @param origin the square this pawn moves from
	 * @param board  the board this pawn is placed on
	 * @param playerOne the color of player one's pieces
	 * @return the list of all currently legal moves
	 */
	public ArrayList<Move> reachableSquares(Square origin, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		int x = origin.getX();
		int y = origin.getY();
		int dy = (playerOne == WHITE && color == WHITE)
				|| (playerOne == BLACK && color == BLACK) ? -1 : 1;
		PieceColor opponentColor = color.getOpposite();

		// Check whether the vertical moves are valid
		if (board.withinBoard(x, y + dy)) {
			// Check square straight ahead
			Square oneAhead = board.getSquare(x, y + dy);
			if (oneAhead.isEmpty())
				if (pawnPromotionIsValid(y, dy, board))
					reachable.add(new Move(origin, oneAhead, this, null, MoveType.PROMOTION));
				else
					reachable.add(new Move(origin, oneAhead, this, null, MoveType.REGULAR));
			// Check whether this pawn can move two squares ahead
			if (board.withinBoard(x, y + 2 * dy)) {
				Square twoAhead = board.getSquare(x, y + 2 * dy);
				if (!hasMoved && oneAhead.isEmpty() && twoAhead.isEmpty())
					reachable.add(new Move(origin, twoAhead, this, null, MoveType.REGULAR));
			}
		}

		// Check whether diagonal moves are valid
		if (board.withinBoard(x - 1, y + dy)) {
			Square westAhead = board.getSquare(x - 1, y + dy);
			if (westAhead.getPiece() != null && westAhead.getPiece().getColor() == opponentColor) {
				if (pawnPromotionIsValid(y, dy, board))
					reachable.add(new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.PROMOTION));
				else if (enPassantIsValid(playerOne, x, y, dy, board))
					reachable.add(new Move(origin, westAhead, this, capturedEnPassant(westAhead, dy, board), MoveType.ENPASSANT));
				reachable.add(new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.REGULAR));
			}
		}

		if (board.withinBoard(x + 1, y + dy)) {
			Square eastAhead = board.getSquare(x + 1, y + dy);
			if (eastAhead.getPiece() != null && eastAhead.getPiece().getColor() == opponentColor) {
				if (pawnPromotionIsValid(y, dy, board))
					reachable.add(new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.PROMOTION));
				else if (enPassantIsValid(playerOne, x, y, dy, board))
					reachable.add(new Move(origin, eastAhead, this, capturedEnPassant(eastAhead, dy, board), MoveType.ENPASSANT));
				reachable.add(new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.REGULAR));
			}
		}
		return reachable;
	}

	@Override
	protected ArrayList<IPiece> enemiesReached(int x, int y, IBoard board, PieceColor opponent, ArrayList<Move> check) {
		ArrayList<IPiece> reach = new ArrayList<IPiece>();
		if (check == null) {return reach;}
		for(Move mov : check) {

			// TODO: 18/03/2018 possible bug here aswell, mov is sometimes null, why?
			if (mov == null) {
				//System.out.println("mov null");
				continue;
			}
			Square to = mov.getTo();
			Square from = mov.getFrom();
			if(to.getX() != from.getX()) {
				if (!to.isEmpty())
					if (to.getPiece().getColor() == opponent && !reach.contains(to.getPiece()))
						reach.add(to.getPiece());
			}
		}
		return reach;
	}

	@Override
	public IPiece copy() {
		Pawn p = new Pawn(this.getColor());
		if (this.hasMoved())
			p.pieceMoved();
		return p;
	}

	/**
	 * Returns a string that represents this pawn.
	 */
	@Override
	public String toString() {
		return "P";
	}
}
