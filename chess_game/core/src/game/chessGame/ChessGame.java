package game.chessGame;

import java.util.ArrayList;

import boardstructure.IBoard;
import boardstructure.Move;
import game.GameType;
import game.listeners.ChessGameListener;
import pieces.IPiece;
import pieces.PieceColor;
import player.AILevel;
import setups.DefaultSetup;

public class ChessGame implements IChessGame {
	IBoard board;
	String player1;
	String player2;
	PieceColor playerOneColor;
	
	/**
	 * GameType gameType. The type of game to be played.
	 * For instance single-player, multi-player o.l.
	 */
	GameType gameType;
	AILevel level;
	PieceColor turn;
	ChessGameListener listener;
	
	
	public ChessGame(String player1, String player2, GameType gameType, PieceColor playerOneColor, AILevel level, ChessGameListener listener) {
		this.player1 = player1;
		if(player2 == null) {
			this.player2 = "computer";
			this.level = level;
		}
		else 
			this.player2 = player2;
		
		this.gameType = gameType;
		this.playerOneColor = playerOneColor;
		this.turn = playerOneColor;
		this.listener = listener;

		//board for standard chess
		this.board = (new DefaultSetup()).getInitialPosition(playerOneColor);
	}
	
	@Override
	public void doTurn(int fromX, int fromY, int toX, int toY) {
		Move m = board.getMove(fromX, fromY, toX, toY);
		if(m.getMovingPiece().getColor() != turn) {
			listener.notYourPieceColor();
		}
		if(board.move(m.getFrom(), m.getTo()) == null) {
			listener.notALegalMove();
		}
				
		//TODO: finish
		
		this.turn = getOtherPieceColor(turn);
	}
	
	@Override
	public PieceColor getOtherPieceColor(PieceColor current) {
		if (current == PieceColor.WHITE)
			return PieceColor.BLACK;
		return PieceColor.WHITE;
	}
	
	@Override
	public GameFinish gameFinished() {
		// TODO Auto-generated method stub
		return null;
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
}
