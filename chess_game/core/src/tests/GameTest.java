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
import pieces.pieceClasses.Pawn;
import pieces.pieceClasses.Rook;

public class GameTest {
	IChessGame game = new ChessGame(null, null, null, null, null, null);
	IBoard board = new Board(8, PieceColor.WHITE);
	IBoard other = new Board(8, PieceColor.WHITE);

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
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.WHITE));
		assertTrue(game.contains(board, sq));	
	}

	@Test
	public void containsFailsIfSquareIsNotInBoard() {
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.BLACK));
		assertFalse(game.contains(board, sq));	
	}

	@Test
	public void fiftyMoveRuleIffiftyMovesOnlyRook() {
		int moves = 50;
		IBoard board = new Board(8, PieceColor.WHITE);
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(0, 0).putPiece(r);
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(0, 2);
		for(int i = 0; i < moves; i++) {
			if(i % 2 == 0)
				board.move(one, two);
			else 
				board.move(two, one);

		}
		assertEquals(moves, board.getHistory().size());
		game.setBoard(board);
		assertTrue(game.fiftyMoves());
	}

	@Test
	public void notFiftyMoveRuleIfPawnMoves() {
		int moves = 50;
		IBoard board = new Board(8, PieceColor.WHITE);
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(0, 0).putPiece(r);
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(0, 2);
		for(int i = 0; i < moves; i++) {
			if(i == 25) {
				Pawn p = new Pawn(PieceColor.WHITE);
				board.getSquare(5, 5).putPiece(p);
				board.move(board.getSquare(5, 5), board.getSquare(5, 4));
				board.move(two, one);
			}
			else {
				if(i % 2 == 0)
					board.move(one, two);
				else 
					board.move(two, one);
			}
		}
		//does two moves at 25
		assertEquals(moves+1, board.getHistory().size());
		game.setBoard(board);
		assertFalse(game.fiftyMoves());
	}


}
