package game.chessGame;

import pieces.PieceColor;

public interface IChessGame {
	
	/**
	 * Do one turn
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 */
	public void doTurn(int fromX, int fromY, int toX, int toY);
	
	/**
	 * Simple method to return opposite of the PieceColor you give
	 * @param PieceColor current
	 * @return PieceColor opposite
	 */
	public PieceColor getOtherPieceColor(PieceColor current);
	
	/**
	 * Tells if the game is finished, and returns a 
	 * string, containing the way that it was finished.
	 * @return
	 */
	public GameFinish gameFinished();

}
