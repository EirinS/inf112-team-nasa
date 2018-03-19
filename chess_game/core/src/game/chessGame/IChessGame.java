package game.chessGame;

import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Rook;

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
	 * Finishes a game, and performs the 
	 * necessary steps.
	 * @param PieceColor turn, the player that loses.
	 */
	public void finishGame(PieceColor turn);
	
	/**
	 * Player whose turn it is, resigns game.
	 */
	public void resign();
	
	/**
	 * Automatic draw if no capture or pawn move 
	 * has been made in the last fifty moves
	 * @return true if 50-move rule, false else.
	 */
	public boolean fiftyMoves();
	
	/**
	 * The player whose turn it is has no legal move,
	 * but isn't in check
	 * @return true if stalemate, false else.
	 */
	public boolean stalemate();
	
	/**
	 * Draw if the same position occurs three times with the
	 * same player to move (can be any time during the game)
	 * @return true if threefoldRepetition, false else.
	 */
	public boolean threefoldRepetition();

	/**
	 * This equality only holds for threefold repetition.
	 * (For instance when you find which pieces you can capture, 
	 * if you can capture one pawn that hasn't moved, you can't capture all).
	 * @param piece
	 * @param other
	 * @return true if they have the same field variables, false else.
	 */
	public boolean piecesAreEqual(IPiece piece, IPiece other);
	
	/**
	 * Check if a board contains a square with equal field variables.
	 * @param board, the IBoard to check in
	 * @param sq, the Square to check for
	 * @return true if it is in board, false else.
	 */
	public boolean contains(IBoard board, Square sq);

}
