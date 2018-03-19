package game.listeners;

public interface ChessGameListener {
	
	/**
	 * Called from ChessGame when you try to move pieces that isn't yours.
	 */
	public void notYourPieceColor();
	
	/**
	 * Called from ChessGame if you try to move to an illegal position.
	 */
	public void notALegalMove();


}
