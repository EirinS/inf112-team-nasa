package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import game.chessGame.ChessGame;
import game.chessGame.IChessGame;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Rook;

public class GameTest {
	IChessGame game = new ChessGame(null, null, null, null, null, null);

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void piecesAreEqualWhenEqualFieldVariablesPiece() {
		assertTrue(game.piecesAreEqual(new Rook(PieceColor.WHITE),(new Rook(PieceColor.WHITE))));
	}
	
	@Test
	public void piecesAreNotEqualWhenNotEqualFieldVariablesPiece() {
		IPiece rook = new Rook(PieceColor.WHITE);
		rook.pieceMoved();
		assertFalse(game.piecesAreEqual(rook, (new Rook(PieceColor.WHITE))));
	}
	
	@Test
	public void containsWorksWithDifferentBoardsButEqualFieldVariablesForSquare() {
		IBoard board = new Board(8, PieceColor.WHITE);
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		IBoard other = new Board(8, PieceColor.WHITE);
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.WHITE));
		assertTrue(game.contains(board, sq));	
	}
	
	@Test
	public void containsFailsIfSquareIsNotInBoard() {
		IBoard board = new Board(8, PieceColor.WHITE);
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		IBoard other = new Board(8, PieceColor.WHITE);
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.BLACK));
		assertFalse(game.contains(board, sq));	
	}


}
