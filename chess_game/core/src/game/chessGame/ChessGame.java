package game.chessGame;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import game.listeners.ChessGameListener;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Pawn;
import pieces.pieceClasses.Rook;
import setups.DefaultSetup;

public class ChessGame implements IChessGame {

	private GameInfo gameInfo;
	private IBoard board;

	private PieceColor turn;
	private ChessGameListener listener;

	private ArrayList<Move> p1history = new ArrayList<>();
	private ArrayList<Move> p2history = new ArrayList<>();

	private ArrayList<IBoard> boardHistory = new ArrayList<>(); 

	public ChessGame(GameInfo gameInfo, ChessGameListener listener) {
		this.gameInfo = gameInfo;
		this.listener = listener;

		// Set first turn and board for standard chess
		turn = gameInfo.getPlayerColor();
		this.board = (new DefaultSetup()).getInitialPosition(gameInfo.getPlayerColor());
	}

	@Override
	public void doTurn(int fromX, int fromY, int toX, int toY) {
		ArrayList<Move> moves = board.getMove(fromX, fromY, toX, toY);
		for (Move m : moves) {
			if (m.getMovingPiece().getColor() != turn) {
				listener.notYourPieceColor();
				return;
			}
			if (board.move(m.getFrom(), m.getTo()).isEmpty()) { //returns empty arrayList
				listener.notALegalMove();
				return;
			}
			board.move(m.getFrom(), m.getTo());	
			if(turn == gameInfo.getPlayerColor())
				p1history.add(m);
			else p2history.add(m);
		}

		//to check for threefold repetition
		boardHistory.add(board);
		this.turn = getOtherPieceColor(turn);
	}

	// WAYS TO END GAMES ---------------------------------------------------------
	/**
	 * Not sure about this yet.
	 */
	public boolean threefoldRepetition() {
		//TODO: not sure about this one yet.
		boolean even = (boardHistory.size()-1) % 2 == 0;
		int start;

		//TODO: might be opposite
		if(even)
			start = 0;
		else 
			start = 1;
		//must be same player to move. same player always moved every other time.

		//current board;
		IBoard current = boardHistory.get(boardHistory.size()-1);

		//no threefoldrepetition if no player made 3 moves
		if(boardHistory.size() < 6) {return false;}
		int count;
		for(int i = boardHistory.size()-3; i >= start; i-=2) {
			count = 0;
			//assumes all boards exists and have same size
			for(Square sq : boardHistory.get(i).getBoard()) {
				if (!contains(current, sq))
					break;
			}
			//found equal board.
			count++;
			//found threefold-repetition
			if (count >= 3)
				return true;

		}
		return false;
	}

	@Override
	public boolean contains(IBoard board, Square sq) {
		for(Square other : board.getBoard()) {
			if (sq.getX() == other.getX() && sq.getY() == other.getY() && piecesAreEqual(sq.getPiece(), other.getPiece()))
				return true;
		}
		return false;
	}

	@Override
	public boolean piecesAreEqual(IPiece piece, IPiece other) {
		if(piece == null || other == null && !(piece == null && other == null))
			return false;
		if (piece.getClass() != other.getClass())
			return false;
		if (piece.getColor() != other.getColor())
			return false;
		if (piece instanceof Pawn || piece instanceof Rook || piece instanceof King)
			if(piece.hasMoved() != other.hasMoved())
				return false;
		if(piece.isInPlay() != other.isInPlay())
			return false;
		return true;
	}


	@Override
	public boolean fiftyMoves() {
		ArrayList<Move> moves = board.getHistory();
		int count = 0;
		for (int i = moves.size()-1; i >= 0; i--) {
			//if a piece was captured, or pawn moved, no draw.
			if (moves.get(i).getCapturedPiece() != null || moves.get(i).getMovingPiece() instanceof Pawn) {
				return false;
			}
			count++;
			if(count >= 50)
				return true;
		}
		return false;
	}
	

	@Override
	public boolean checkmate() {
		if (board.getAvailableMoves(turn).isEmpty()) {
			ArrayList<IPiece> threat = board.piecesThreatenedByOpponent(turn, getOtherPieceColor(turn));
			for(IPiece p : threat) {
				if (p instanceof King) {
					return true;
				}
			}
		}
		return false;

	}
	
	
	@Override
	public boolean impossibleCheckmate() {
		ArrayList<Square> pieceSqs = new ArrayList<>();  
		for(Square sq : board.getBoard()) {
			if(!sq.isEmpty())
				pieceSqs.add(sq);
		}
		
		//only way to have 2 pieces left, is 2 kings.
		if(pieceSqs.size() == 2) {
			return true;
		} else if(pieceSqs.size() == 3) {
			for(Square p : pieceSqs)
				//if last piece is bishop or knight, no check-mate can be reached. Automatic draw.
				if (p.getPiece() instanceof Bishop || p.getPiece() instanceof Knight)
					return true;
		} else if(pieceSqs.size() == 4) {
			return fourPiecesCausesAutomaticDraw(pieceSqs)	;		
		}
		return false;
	}
	
	/**
	 * Checks if the two pieces besides the king are two
	 * bishops that are on the same color-squares, but for
	 * different players.
	 * @param pieceSqs
	 * @return true if draw, false else
	 */
	public boolean fourPiecesCausesAutomaticDraw(ArrayList<Square> pieceSqs) {
		ArrayList<Square> bishops = new ArrayList<>();
		//find bishops
		for(Square sq : pieceSqs) {
			if(!(sq.getPiece() instanceof Bishop) && !(sq.getPiece() instanceof King))
				return false;
			if(sq.getPiece() instanceof Bishop) {
				bishops.add(sq);
			}
		}
		//need two bishops for further checks
		if(bishops.size() != 2)
			return false;
		if(bishops.get(0).getPiece().getColor() == bishops.get(1).getPiece().getColor())
			return false;
		if(bishops.get(0).squareIsWhite() != bishops.get(1).squareIsWhite()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean stalemate() {
		if (board.getAvailableMoves(turn).isEmpty()) {
			//put in if you need check for stale-mate (king not in check)
			/*
			ArrayList<IPiece> threat = board.piecesThreatenedByOpponent(turn, getOtherPieceColor(turn));
			for(IPiece p : threat) {
				if (p instanceof King) {
					return false;
				}
			} */
			return true;
		}
		return false;
	}

	@Override
	public void resign() {
		finishGame(turn);
	}

	// WAYS TO END GAMES - END ---------------------------------------------------------

	@Override
	public void finishGame(PieceColor turn) {
		if(turn == null) {
			//draw
		} else if (turn == gameInfo.getPlayerColor()) {
			//player1 lost
		} else {
			//player2 lost
		}
	}

	@Override
	public PieceColor getOtherPieceColor(PieceColor current) {
		if (current == PieceColor.WHITE)
			return PieceColor.BLACK;
		return PieceColor.WHITE;
	}




	/**
	 * Method that calculates the new rating for a player after a game
	 * 
	 * @param rating1 Rating of the player
	 * @param rating2 Rating of the adversary
	 * @param win_lose_draw 1 if player wins, 2 if player loses and 3 if player draws
	 * 
	 * @return newRating The players new rating
	 */
	public static int calculateNewRating(int rating1, int rating2, int win_lose_draw)
	{
		if(win_lose_draw > 3 || win_lose_draw < 1) 
		{
			throw new IllegalArgumentException("Illegal input on win_lose_draw - Must be either 1, 2 or 3");
		}

		// Sensitivity parameter
		int K = 32;

		// Intermediary calculations
		double r1 = Math.pow(10, rating1/400.0);
		double r2 = Math.pow(10, rating2/400.0);		
		double expectedScore1 = r1 / (r1 + r2);		
		double s;

		if(win_lose_draw == 1) 		 // win
		{
			s = 1;
		}
		else if(win_lose_draw == 2)  // loss
		{
			s = 0;
		}
		else					     // Draw
		{
			s = 0.5;
		}

		double newRating = rating1 + ( K * (s - expectedScore1));

		return (int) newRating;
	}

	@Override
	public IBoard getBoard() {
		return board;
	}

	@Override
	public void setBoard(IBoard board) {
		this.board = board;
		
	}

}
