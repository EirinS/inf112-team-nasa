package game;

import boardstructure.IBoard;
import boardstructure.Move;
import pieces.PieceColor;
import player.AILevel;
import setups.DefaultSetup;

public class ChessGame implements CheckerboardListener {
	IBoard board;
	String player1;
	String player2;
	PieceColor playerOneColor;
	GameType gameType;
	AILevel level;

	public ChessGame(String player1, String player2, GameType gameType, PieceColor playerOneColor, AILevel level) {
		this.player1 = player1;
		if(player2 == null) {
			this.player2 = "computer";
			this.level = level;
		}
		else 
			this.player2 = player2;
		this.gameType = gameType;


		this.playerOneColor = playerOneColor;

		//board for standard chess
		this.board = (new DefaultSetup()).getInitialPosition(playerOneColor);
	}

	@Override
	public void onDragPieceStarted(int x, int y) {

	}

	@Override
	public void onMoveRequested(int fromX, int fromY, int toX, int toY) {

	}


}
