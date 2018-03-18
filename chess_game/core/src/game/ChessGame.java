package game;

import boardstructure.IBoard;
import boardstructure.Move;
import pieces.PieceColor;
import setups.DefaultSetup;

public class ChessGame implements CheckerboardListener {
	IBoard board;
	String player1;
	String player2;
	PieceColor playerOneColor;
	GameType gameType;
	
	public ChessGame(String player1, String player2, GameType gameType, PieceColor playerOneColor) {
		this.player1 = player1;
		if(player2 == null)
			this.player2 = "computer";
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
