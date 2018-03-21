package game.chessGame;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import game.Chess;
import game.GameType;
import game.listeners.ChessGameListener;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Pawn;

import pieces.pieceClasses.Rook;

import player.AI;
import register.Player;
import register.PlayerRegister;
import setups.DefaultSetup;

public class ChessGame implements IChessGame {
	private GameInfo gameInfo;
	private IBoard board;
	private AI computerAI;

	private ChessGameListener listener;

	private ArrayList<IBoard> boardHistory = new ArrayList<>();

	private int playerSeconds, opponentSeconds;
	private Timer playerTimer, opponentTimer;
	private boolean playerTimerRunning, opponentTimerRunning;

	public ChessGame(GameInfo gameInfo, ChessGameListener listener) {
		this.gameInfo = gameInfo;
		this.listener = listener;
		playerSeconds = 300;
		opponentSeconds = 300;

		// Set first turn and board for standard chess
		this.board = (new DefaultSetup()).getInitialPosition(gameInfo.getPlayerColor());

		// Load AI
		if (gameInfo.getLevel() != null) {
			computerAI = gameInfo.getLevel().getAI(gameInfo.getPlayerColor().getOpposite());
		}

		// Start timer!
		turnTimer();
	}

	private void turnTimer() {
		if (board.getTurn() == gameInfo.getPlayerColor()) {
			if (!playerTimerRunning) {
				playerTimer = new Timer();
				playerTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						playerSeconds -= 1;
						if (listener != null) listener.turnTimerElapsed();
						if (playerSeconds == 0) {
							finishGame(board.getTurn());
						}
					}
				},0, 1000);
				playerTimerRunning = true;
			}
			if (opponentTimerRunning) {
				opponentTimer.cancel();
				opponentTimerRunning = false;
			}
		} else {
			if (!opponentTimerRunning) {
				opponentTimer = new Timer();
				opponentTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						opponentSeconds -= 1;
						if (listener != null) listener.turnTimerElapsed();
						if (opponentSeconds == 0) {
							finishGame(gameInfo.getPlayerColor().getOpposite());
						}
					}
				},0, 1000);
				opponentTimerRunning = true;
			}
			if (playerTimerRunning) {
				playerTimer.cancel();
				playerTimerRunning = false;
			}
		}
	}

	@Override
	public ArrayList<Move> getLegalMoves(int x, int y) {
		// TODO: 21/03/2018 sometimes this bugs, not sure why. NullPointerException
		// TODON'T: I think it's because AI looks for move, and there is none. This is when check-mate.
		// also if you look for move. I don't really think it's a bug. It's just not done yet.
		Square square = board.getSquare(x, y);
		if (square.getPiece().getColor() != board.getTurn()) return new ArrayList<>();
		return square.getPiece().getLegalMoves(square, board, gameInfo.getPlayerColor());
	}

	@Override
	public void doTurn(int fromX, int fromY, int toX, int toY) {
		ArrayList<Move> moves = board.move(fromX, fromY, toX, toY);
		if (moves.isEmpty()) {
			listener.illegalMovePerformed(fromX, fromY);
			return;
		}
		listener.moveOk(moves);
		turnTimer();
		boardHistory.add(board.copy());
		
		//this player is in checkmate, game is finished
		if (checkmate()) {
			finishGame(board.getTurn().getOpposite());
			return;
		}
		if (isTie()) {
			finishGame(null);
			return;
		}

		// Check if AI should do move
		aiMove();
	}

	public void aiMove() {
		if (computerAI == null) return;
		if (computerAI.getPieceColor() == board.getTurn()) {
			Move move = computerAI.calculateMove(board);
			doTurn(move.getFrom().getX(), move.getFrom().getY(), move.getTo().getX(), move.getTo().getY());
		}
	}

	@Override
	public void finishGame(PieceColor turn) {
		//stop clock
		if (playerTimerRunning) playerTimer.cancel();
		if (opponentTimerRunning) opponentTimer.cancel();
		
		Player p = gameInfo.getPlayer();
		Player o = gameInfo.getOpponent();
		if (turn == null) {
			updateRatings(p, o, 3);
			listener.draw();
		} else if (turn == gameInfo.getPlayerColor()) {
			//player, whose color is turn, lost
			listener.loss();
			updateRatings(p, o, 2);
		} else {
			//player, whose color is turn, won
			listener.win();
			updateRatings(p, o, 1);
		}
	}

	// WAYS TO END GAMES ---------------------------------------------------------

	@Override
	public boolean isTie() {
		return fiftyMoves() || impossibleCheckmate() || stalemate() || threefoldRepetition();
	}

	@Override
	public boolean threefoldRepetition() {
		//no threefoldrepetition if no player made 3 moves
		if(boardHistory.size() < 5) {
			return false;
		}

		//current board;
		IBoard current = boardHistory.get(boardHistory.size()-1);
		int count = 1;
		//assumes all boards exists and have same size
		for(int i = boardHistory.size()-3; i >= 0; i-=2) {
			if(isSame(current, boardHistory.get(i))) {
				//found equal board.
				count++;
				//found threefold-repetition
				if (count >= 3) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Two boards are equal
	 * @param board
	 * @param other
	 * @return
	 */
	public boolean isSame(IBoard board, IBoard other) {
		if(board.getSquares().size() != other.getSquares().size()){
			return false;
		}
		for(Square sq : board.getSquares())
			if(!contains(other, sq)) {
				return false;
			}
		/*
		for(Square sq : other.getBoard())
			if(!contains(board, sq))
				return false; */
		return true;
	}

	@Override
	public boolean contains(IBoard board, Square sq) {
		for(Square other : board.getSquares()) {
			if (sq.getX() == other.getX() && sq.getY() == other.getY()) {
				if (sq.isEmpty() && other.isEmpty())
					return true;
				if(!sq.isEmpty() && !other.isEmpty())
					if (sq.getClass() == other.getClass() && sq.getPiece().getColor() == other.getPiece().getColor())
						return true;
			}
		}
		return false;
	}

	@Override
	public boolean fiftyMoves() {
		ArrayList<Move> moves = board.getHistory();
		int count = 0;
		for (int i = moves.size() - 1; i >= 0; i--) {
			//if a piece was captured, or pawn moved, no draw.
			if (moves.get(i).getCapturedPiece() != null || moves.get(i).getMovingPiece() instanceof Pawn) {
				return false;
			}
			count++;
			if (count >= 50)
				return true;
		}
		return false;
	}


	@Override
	public boolean checkmate() {
		if (board.getAvailableMoves(board.getTurn()).isEmpty()) {
			ArrayList<IPiece> threat = board.piecesThreatenedByOpponent(board.getTurn(), board.getTurn().getOpposite());
			for (IPiece p : threat) {
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
		for (Square sq : board.getSquares()) {
			if (!sq.isEmpty())
				pieceSqs.add(sq);
		}

		//only way to have 2 pieces left, is 2 kings.
		if (pieceSqs.size() == 2) {
			return true;
		} else if (pieceSqs.size() == 3) {
			for (Square p : pieceSqs)
				//if last piece is bishop or knight, no check-mate can be reached. Automatic draw.
				if (p.getPiece() instanceof Bishop || p.getPiece() instanceof Knight)
					return true;
		} else if (pieceSqs.size() == 4) {
			return fourPiecesCausesAutomaticDraw(pieceSqs);
		}
		return false;
	}

	/**
	 * Checks if the two pieces besides the king are two
	 * bishops that are on the same color-squares, but for
	 * different players.
	 *
	 * @param pieceSqs
	 * @return true if draw, false else
	 */
	public boolean fourPiecesCausesAutomaticDraw(ArrayList<Square> pieceSqs) {
		ArrayList<Square> bishops = new ArrayList<>();
		//find bishops
		for (Square sq : pieceSqs) {
			if (!(sq.getPiece() instanceof Bishop) && !(sq.getPiece() instanceof King))
				return false;
			if (sq.getPiece() instanceof Bishop) {
				bishops.add(sq);
			}
		}
		//need two bishops for further checks
		if (bishops.size() != 2)
			return false;
		if (bishops.get(0).getPiece().getColor() == bishops.get(1).getPiece().getColor())
			return false;
		if (bishops.get(0).squareIsWhite() != bishops.get(1).squareIsWhite()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean stalemate() {
		if (board.getAvailableMoves(board.getTurn()).isEmpty()) {
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
		finishGame(board.getTurn());
	}

	// WAYS TO END GAMES - END ---------------------------------------------------------


	/**
	 * Method that calculates the new rating for a player after a game
	 *
	 * @param rating1       Rating of the player
	 * @param rating2       Rating of the adversary
	 * @param win_lose_draw 1 if player wins, 2 if player loses and 3 if player draws
	 * @return newRating The players new rating
	 */
	public static int calculateNewRating(int rating1, int rating2, int win_lose_draw) {
		if (win_lose_draw > 3 || win_lose_draw < 1) {
			throw new IllegalArgumentException("Illegal input on win_lose_draw - Must be either 1, 2 or 3");
		}

		// Sensitivity parameter
		int K = 32;

		// Intermediary calculations
		double r1 = Math.pow(10, rating1 / 400.0);
		double r2 = Math.pow(10, rating2 / 400.0);
		double expectedScore1 = r1 / (r1 + r2);
		double s;

		if (win_lose_draw == 1)         // win
		{
			s = 1;
		} else if (win_lose_draw == 2)  // loss
		{
			s = 0;
		} else                         // Draw
		{
			s = 0.5;
		}

		double newRating = rating1 + (K * (s - expectedScore1));

		return (int) newRating;
	}

	/**
	 * Precondition: game is against AI.
	 * Update for single player game.
	 * @param p
	 * @param win_lose_draw
	 */
	private void updateSinglePlayerRating(Player p, int win_lose_draw) {
		int newRating = calculateNewRating(p.getRating(), computerAI.getRating(), win_lose_draw);
		Chess.getPlayerRegister().updatePlayerRating(p.getName(), newRating, win_lose_draw);
	}

	@Override
	public void updateRatings(Player p, Player o, int win_lose_draw) {
		if (o == null) {
			updateSinglePlayerRating(p, win_lose_draw);
			return;
		}

		String pName = p.getName();
		String oName = o.getName();  	
		int pRating = p.getRating();
		int oRating = o.getRating();
		
		PlayerRegister pr = Chess.getPlayerRegister();
		
		int op_win_lose_draw;
		if(win_lose_draw == 1)
			op_win_lose_draw = win_lose_draw+1;
		else if(win_lose_draw == 2)
			op_win_lose_draw = win_lose_draw-1;
		else 
			op_win_lose_draw = win_lose_draw;
			
		int pNewRating = calculateNewRating(pRating, oRating, win_lose_draw);
		int oNewRating = calculateNewRating(oRating, pRating, op_win_lose_draw);
		
		pr.updatePlayerRating(pName, pNewRating, win_lose_draw);
		pr.updatePlayerRating(oName, oNewRating, op_win_lose_draw);
		
	}

	@Override
	public ArrayList<Square> getSquares() {
		return board.getSquares();
	}

	@Override
	public PieceColor getTurn() {
		return board.getTurn();
	}

	@Override
	public void setBoard(IBoard board) {
		this.board = board;
	}

	public Move getLastMove() {
		return board.getLastMove();
	}

	@Override
	public void setBoardHistory(ArrayList<IBoard> boardHistory) {
		this.boardHistory = boardHistory;
	}

	public int getPlayerSeconds() {
		return playerSeconds;
	}

	public int getOpponentSeconds() {
		return opponentSeconds;
	}

	public String formatTime(int seconds) {
		return String.format("%02d:%02d", seconds / 60, seconds % 60);
	}

	@Override
	public ArrayList<IBoard> getBoardHistory() {
		return boardHistory;
	}
}
