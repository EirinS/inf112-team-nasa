package game.chessGame;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Rook;

import java.util.ArrayList;

public interface IChessGame {

	/**
	 * Gets all the legal moves from a given x and y.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return Legal moves from (x, y).
	 */
	ArrayList<Move> getLegalMoves(int x, int y);

	/**
	 * Do one turn
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 */
	void doTurn(int fromX, int fromY, int toX, int toY);
	
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
	 * Precondition: you can never capture king 
	 * (check-mate happens before and game is ended),
	 * hence, two kings will always be on the board.
	 * Automatic draw if:
	 * - Only kings left on the board
	 * - King and bishop vs king
	 * - King and knight vs king
	 * - King and bishop vs king and bishop (bishops on same colored squares)
	 * @return true if draw, false else
	 */
	public boolean impossibleCheckmate();
	
	
	/**
	 * Precondition: check for check-mate first! This method 
	 * does not take account for whether the king is in check or not,
	 * so it'll return true for check-mate and stale-mate, if you don't
	 * check for check-mate first!
	 * The player whose turn it is has no legal move,
	 * but isn't in check
	 * @return true if stale-mate, false else.
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
	
	/**
	 * Check if it is checkmate.
	 * Checkmate occurs if you're in check and no legal
	 * positions can be made.
	 * @return true, if checkmate, false else
	 */
	public boolean checkmate();
	
	/**
	 * Get the board connected to this game
	 * @return IBoard board
	 */
	public IBoard getBoard();
	
	/**
	 * Set the board of this game to the desired board.
	 * @param board, IBoard board, the desired board.
	 */
	public void setBoard(IBoard board);
}
