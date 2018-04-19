package boardstructure;

import java.util.ArrayList;
import java.util.List;

import game.chessGame.GameType;
import pieces.IPiece;
import pieces.PieceColor;
import player.AI;

public interface IBoard {

	/**
	 * Sets the opponent AI.
	 * @param ai AI to set.
	 */
	void setAI(AI ai);

	/**
	 * Sets the board listener.
	 * @param listener Board listener
	 */
	void setListener(BoardListener listener);

	/**
	 * Width of board
	 * @return width
	 */
	int getWidth();
	
	/**
	 * Height of board
	 * @return height
	 */
	int getHeight();
	
	/**
	 * Dimensions of board.
	 * @return dimension
	 */
	int getDimension();
	
	/**
	 * Find a king on the board.
	 * @param kingColor, color of king you want to find.
	 * @return the square the king you want to find is in,
	 * null if no king was found.
	 */
	Square getKingPos(PieceColor kingColor);
	
	/**
	 * Get all bishops on the axis specified. (used for chess960)
	 * @param y
	 * @return x position of the rook
	 */
	ArrayList<Integer> getBishopPos(int y);
	
	/**
	 * Find the rooks on the board.
	 * @param rookColor, color of king you want to find.
	 * @return ArrayList<Square> the squares the rooks you want to find is in,
	 * null if no king was found.
	 */
	ArrayList<Square> getRookPos(PieceColor rookColor);
	
	/**
	 * Get a specific square in board
	 * @return square
	 */
	Square getSquare(int x, int y);
	
	/**
	 * Get the integer value, that is the index of the
	 * square in the ArrayList representation of board.
	 * @param x
	 * @param y
	 * @return index in ArrayList representation of board.
	 */
	int getBoardPlacement(Square sq);
	
	/**
	 * Gives all the squares in the board.
	 * @return all squares in board.
	 */
	ArrayList<Square> getSquares();
	
	/**
	 * Check if a piece is inside the board, and if there is no other piece there.
	 * @param sq, square you are checking
	 * @return true if legal move, false else
	 */
	boolean movable(Square sq);
	
	/**
	 * Add a square in the square's given position.
	 * @param sq
	 */
	void addSquare(Square sq);
	
	/**
	 * Is this position within the board?
	 * @param x, x-coordinate
	 * @param y, y-coordinate
	 * @return true if within board, false if not.
	 */
	boolean withinBoard(Square sq);
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return true if within board, false if not.
	 */
	boolean withinBoard(int x, int y);
	
	/**
	 * @return the GameType of this game
	 */
	GameType getGameType();
	
	/**
	 * Returns all the pieces than can be captured by white.
	 * @param PieceColor player, your piece color
	 * @param PieceColor opponent, the color that threatens you.
	 * @return ArrayList<IPiece>, all pieces threatened by white.
	 */
	ArrayList<IPiece> piecesThreatenedByOpponent(PieceColor player, PieceColor opponent);

	/**
	 * Move a piece to a legal position on the board.
	 * Assumes the piece chosen is a piece of correct color.
	 * @param fromX From x position
	 * @param fromY From y position
	 * @param toX To x position
	 * @param toY To y position
	 * @return List of moves executed on the board, empty if no illegal move.
	 */
	void move(int fromX, int fromY, int toX, int toY);

	/**
	 * Move a piece to a legal position on the board.
	 * Assumes the piece chosen is a piece of correct color.
	 * @param fromX From x position
	 * @param fromY From y position
	 * @param toX To x position
	 * @param toY To y position
	 * @param ignoreTurn Wether the board should ignore who's turn it is.
	 * @return List of moves executed on the board, empty if no illegal move.
	 */
	void move(int fromX, int fromY, int toX, int toY, boolean ignoreTurn);

	/**
	 * Move a piece to a legal position on the board.
	 * Assumes the piece chosen is a piece of correct color.
	 * @param start, the position the piece had
	 * @param end, the position the piece goes to.
	 * @return List of moves executed on the board, empty if no illegal move.
	 */
	void move(Square start, Square end);


	/**
	 * Move a piece to a legal position on the board.
	 * Assumes the piece chosen is a piece of correct color.
	 * @param start, the position the piece had
	 * @param end, the position the piece goes to.
	 * @param ignoreTurn Wether the board should ignore who's turn it is.
	 * @param listener Board listener
	 * @return List of moves executed on the board, empty if no illegal move.
	 */
	void move(Square start, Square end, boolean ignoreTurn);

	/**
	 * Performs promotion on the board given a move and a piece class.
	 * @param m Promotion move that was sent from the request.
	 * @param piece Piece to promote to.
	 */
	void performPromotion(Move m, PromotionPiece piece);
	
	/**
	 * This method returns the algebraic notation of all moves made.
	 * @return ArrayList<String> history of all moves made.
	 */
	ArrayList<Move> getHistory();

	/**
	 * This method returns the last successfull move.
	 * @return Last move, null if no moves.
	 */
	Move getLastMove();
	
	/**
	 * Makes a copy of this board and the squares in it.
	 * @return An IBoard board copy
	 */
	IBoard copy();

	/**
	 *
	 * @param playerColor color of the player you want to have the moves from
	 * @return available moves for the given player color
	 */
	List<Move> getAvailableMoves(PieceColor playerColor);

	/**
	 * Gets the current turn.
	 * @return Turn
	 */
	PieceColor getTurn();
	
	/**
	 * @return PieceColor color of playerOne in this game.
	 */
	PieceColor getPlayerOne();
	
	/**
	 * Set the turn of this board to be the
	 * given turn.
	 * @param turn
	 */
	void setTurn(PieceColor turn);
	
	/**
	 * Set the history of this board to be the given history.
	 * @param history
	 */
	void setHistory(ArrayList<Move> history);
	
	void printOutBoard();
}

