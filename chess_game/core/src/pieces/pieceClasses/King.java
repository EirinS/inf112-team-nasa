package pieces.pieceClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import game.chessGame.GameType;
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
		if(castling(board.getSquare(x, y), board).isEmpty()) {
			return moves;
		}else { 
			moves.addAll(castling(board.getSquare(x, y), board));
		}
		return moves;
	}

	@Override
	public ArrayList<IPiece> enemyPiecesReached(int x, int y, IBoard board, PieceColor opponent){
		ArrayList<Move> check = reachable(x, y, board.getSquare(x, y), board);
		return enemiesReached(x, y, board, opponent, check);
	}

	/**
	 * Method to see if all squares you must have free for a castling i empty.
	 * @param start int-value of the start x-pos you want to search from (included)
	 * @param end int-value of the end x-pos you want to search to (included)
	 * @param yPos int-value of the y-pos you have
	 * @param board the board
	 * @return true if all squares are empty, false else.
	 */
	private boolean allSquaresForCastlingAreEmpty(int start, int end, int yPos, IBoard board) {
		for(int i = start; i <= end; i++)
			if(!board.getSquare(i, yPos).isEmpty())
				return false;
		return true;
	}

	/**
	 * Checks if a rook is in the given position, and if it has moved.
	 * @param x, the x location
	 * @param y, the y location
	 * @param board, the board
	 * @return true if rook is there and hasn't moved, false else.
	 */
	private boolean rookCanDoCastling(int x, int y, IBoard board) {
		if(board.getSquare(x, y).isEmpty())
			return false;

		IPiece p = board.getSquare(x, y).getPiece();
		if (p instanceof Rook && !p.hasMoved())
			return true;
		return false;
	}

	/**
	 * Method to tell if king is in check on it's way
	 * @param next
	 * @param origin
	 * @param board
	 * @return
	 */
	private boolean kingIsInCheck(Square next, Square origin, IBoard board) {
		IBoard testBoard = board.copy();
		testBoard.getSquare(next.getX(), next.getY()).putPiece((testBoard.getSquare(origin.getX(), origin.getY()).takePiece()));
		return testBoard.piecesThreatenedByOpponent(getColor(), getColor().getOpposite()).contains(testBoard.getKingPos(getColor()).getPiece());
	}

	/**
	 * Help-method to tell if king is threatened and can't do castling.
	 * @param start, x position you look from
	 * @param end, x position you look to
	 * @param yPos,  y position of piece
	 * @param board, the board
	 * @return true if castling can be done, false else.
	 */
	private boolean kingCanDoCastling(int start, int end, int yPos, IBoard board, boolean rightSide) {
		for(int i = start; i <= end; i++) {
			if(rightSide) {
				if(kingIsInCheck(board.getSquare(i, yPos), board.getSquare(start, yPos), board))
					return false;
			} else
				if(kingIsInCheck(board.getSquare(i, yPos), board.getSquare(end, yPos), board))
					return false;
		}
		return true;

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
		//conditions that make castling illegal
		// king moved || king is in check
		if(hasMoved() || threatensKing(board.piecesThreatenedByOpponent(getColor(), getColor().getOpposite()))) {return new ArrayList<>();}
		if(board.getGameType() == GameType.CHESS960) {
			return getChess960Castling(origin, board);
		} else {
			return getRegularChessCastling(origin, board);	
		}
	}
	/**
	 * Method to get how to move and what to check for in castling for chess960.
	 * @param origin
	 * @param board
	 * @return the moves you can do for castling.
	 */
	private ArrayList<Move> getChess960Castling(Square origin, IBoard board){
		//sorry stygg kode, love det funke
		
		ArrayList<Move> legalCastlingMoves = new ArrayList<>();
		//tells me how much extra to move if colors of pieces are flipped on the board.
		int dx = board.getPlayerOne() == PieceColor.WHITE ? 0 : -1;
		for(Square fstRook : board.getRookPos(getColor())) {
			if(!fstRook.getPiece().hasMoved()) {
				boolean rightSide = fstRook.getX() > origin.getX() ? true : false;
				MoveType type = ((board.getPlayerOne() == PieceColor.WHITE && rightSide) 
						|| (board.getPlayerOne() == PieceColor.BLACK && !rightSide))
						? MoveType.KINGSIDECASTLING : MoveType.QUEENSIDECASTLING;
				
				if(hasEmptyRoadToCastling(origin, fstRook, board, rightSide)) {
					if(rightSide) {
						if(origin.getX() == 6 + dx - 1)
							continue;
						legalCastlingMoves.add(new Move(origin, board.getSquare(6 + dx, origin.getY()), this, null, type));
					}
					else {
						if(origin.getX() == 2 + dx + 1)
							continue;
						legalCastlingMoves.add(new Move(origin, board.getSquare(2+dx, origin.getY()), this, null, type));
					}

				}
			}
		}
		return legalCastlingMoves;

	}

	private boolean hasEmptyRoadToCastling(Square origin, Square rookSq, IBoard board, boolean rightSide) {
		int y = origin.getY();
		if(rightSide) {
			for(int x = origin.getX()+1; x <= 6; x++) {
				if(!board.getSquare(x, y).isEmpty())
					if (rookSq.getX() != x)
						return false;


			}
		}else {
			for(int x = origin.getX()-1; x >= 2; x--) {
				if(!board.getSquare(x, y).isEmpty())
					if(rookSq.getX() != x)
						return false;
			}
		}
		return true;
	}


	/**
	 * Method to get how to move and what to check for in castling for regular chess.
	 * @param origin
	 * @param board
	 * @return the moves you can do for castling.
	 */
	private ArrayList<Move> getRegularChessCastling(Square origin, IBoard board){
		ArrayList<Move> legalCastlingMoves = new ArrayList<>();
		//If white is at bottom, we have a different setup than if black is.
		if(board.getPlayerOne() == PieceColor.WHITE) {
			if(allSquaresForCastlingAreEmpty(origin.getX()-3, origin.getX()-1, origin.getY(), board) && rookCanDoCastling(0, origin.getY(), board))
				if(kingCanDoCastling(origin.getX()-3, origin.getX(), origin.getY(), board, false))
					legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()-2, origin.getY()), this, null, MoveType.QUEENSIDECASTLING));
			if(allSquaresForCastlingAreEmpty(origin.getX()+1,origin.getX()+2, origin.getY(), board) && rookCanDoCastling(7, origin.getY(), board))
				if(kingCanDoCastling(origin.getX(), origin.getX()+2, origin.getY(), board, true))
					legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()+2, origin.getY()), this, null, MoveType.KINGSIDECASTLING));
		}
		//Black is at the bottom.
		else {
			if (allSquaresForCastlingAreEmpty(origin.getX()-2,origin.getX()-1, origin.getY(), board) && rookCanDoCastling(0, origin.getY(), board))
				if(kingCanDoCastling(origin.getX()-2, origin.getX(), origin.getY(), board, false))
					legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()-2, origin.getY()), this, null, MoveType.KINGSIDECASTLING));
			if (allSquaresForCastlingAreEmpty(origin.getX()+1,origin.getX()+3, origin.getY(), board) && rookCanDoCastling(7, origin.getY(), board))
				if(kingCanDoCastling(origin.getX(),origin.getX()+2, origin.getY(), board, true))
					legalCastlingMoves.add(new Move(origin, board.getSquare(origin.getX()+2, origin.getY()), this, null, MoveType.QUEENSIDECASTLING));
		}
		return legalCastlingMoves;
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

	/**
	 * Precondition: All positions that are moved to are empty and possible to move to.
	 * Castling is an OK move.
	 * @param origin
	 * @param next
	 * @param type
	 * @param board
	 * @return Returns rook move if everything went well, null if error.
	 */
	public Move moveCastling(Square origin, Square next, MoveType type, IBoard board) {
		if (type != MoveType.QUEENSIDECASTLING && type != MoveType.KINGSIDECASTLING)
			throw new IllegalArgumentException("MoveType is wrong! Must be MoveType.KINGSIDECASTLING, or MoveType.QUEENSIDECASTLING");

		// Moves king
		next.putPiece(origin.movePiece());

		// Moves rook
		Square rookSq, destSq;
		Move rookMove = null;
		ArrayList<Square> rookPos = board.getRookPos(getColor());
		int x = 0;
		
		//check where to start the move from.
		for(Square rookSquare : rookPos) {
			if(rookSquare.getX() > origin.getX()) {
				if((type == MoveType.KINGSIDECASTLING && board.getPlayerOne() == PieceColor.WHITE) || (type == MoveType.QUEENSIDECASTLING && board.getPlayerOne() == PieceColor.BLACK)) {
					x = rookSquare.getX();
				}
			} else if (rookSquare.getX() < origin.getX()) {
				if((type == MoveType.QUEENSIDECASTLING && board.getPlayerOne() == PieceColor.WHITE) || (type == MoveType.KINGSIDECASTLING && board.getPlayerOne() == PieceColor.BLACK)) {
					x = rookSquare.getX();
				}	
			}
		}


		switch (type) {
		case KINGSIDECASTLING:
			if(board.getPlayerOne() == PieceColor.WHITE) {
				rookSq = board.getSquare(x, origin.getY());
				destSq = board.getSquare(5, rookSq.getY());
			} else {
				rookSq = board.getSquare(x, origin.getY());
				destSq = board.getSquare(2, origin.getY());
			}
			rookMove = getMove(rookSq, destSq);
			destSq.putPiece(rookSq.movePiece());
			break;
		case QUEENSIDECASTLING:
			if(board.getPlayerOne() == PieceColor.WHITE) {
				rookSq = board.getSquare(x, origin.getY());
				destSq = board.getSquare(3, origin.getY());
			}
			else {
				rookSq = board.getSquare(x, origin.getY());
				destSq = board.getSquare(4, origin.getY());
			}
			rookMove = getMove(rookSq, destSq);
			destSq.putPiece(rookSq.movePiece());
			break;
		}
		return rookMove;
	}

	@Override
	public IPiece copy() {
		King k = new King(this.getColor());
		if (this.hasMoved())
			k.pieceMoved();
		return k;
	}

	@Override
	public String toString() {
		return "K";
	}
}
