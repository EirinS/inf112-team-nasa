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
	
	private int dy;

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
	 * Get en passant move if possible. Get null if not possible.
	 * @param x, current x
	 * @param y, current y
	 * @param board, current board
	 * @return null if no valid passant, Move m of MoveType ENPASSANT if valid.
	 */
	public Move getPassantMove(int x, int y, IBoard board) {	
		//TODO 22/03 Bug: Moves where pawns captures another pawn are sometimes not registered in moves?
		// Get previous move
		ArrayList<Move> moves = board.getHistory();
		if (moves.size() == 0) return null;
		Move previous = moves.get(moves.size() - 1);
		
		//can do en passant if a "jumping" pawn is next to you, and not of your color.
		int toX = previous.getTo().getX();
		if((x+1 == toX || x-1 == toX) && previous.getMoveType() == MoveType.PAWNJUMP && previous.getMovingPiece().getColor() != this.getColor()) {
			return new Move(board.getSquare(x, y), board.getSquare(toX, y + getDy(board)), this, board.getSquare(toX, previous.getTo().getY()).getPiece(), MoveType.ENPASSANT);
		}
		return null;		
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
	 * returns the legal direction for this pawn to move in.
	 * @param board, to find where white and black is located
	 * @return -1 if moving down, 1 if moving up (0,0 top left corner)
	 */
	private int getDy(IBoard board) {
		return (board.getPlayerOne() == WHITE && color == WHITE)
		|| (board.getPlayerOne() == BLACK && color == BLACK) ? -1 : 1;
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
		int dy = getDy(board);
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
					reachable.add(new Move(origin, twoAhead, this, null, MoveType.PAWNJUMP));
			}
		}

		// Check whether diagonal moves are valid
		if (board.withinBoard(x - 1, y + dy)) {
			Square westAhead = board.getSquare(x - 1, y + dy);
			if (getPassantMove(x, y, board) != null)
				reachable.add(getPassantMove(x, y, board));
			if (westAhead.getPiece() != null && westAhead.getPiece().getColor() == opponentColor) {
				if (pawnPromotionIsValid(y, dy, board))
					reachable.add(new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.PROMOTION));
				reachable.add(new Move(origin, westAhead, this, westAhead.getPiece(), MoveType.REGULAR));
			}
		}

		if (board.withinBoard(x + 1, y + dy)) {
			if (getPassantMove(x, y, board) != null)
				reachable.add(getPassantMove(x, y, board));
			Square eastAhead = board.getSquare(x + 1, y + dy);
			if (eastAhead.getPiece() != null && eastAhead.getPiece().getColor() == opponentColor) {
				if (pawnPromotionIsValid(y, dy, board))
					reachable.add(new Move(origin, eastAhead, this, eastAhead.getPiece(), MoveType.PROMOTION));
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
