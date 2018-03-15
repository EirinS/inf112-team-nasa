package tests.pieceClassesTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import boardstructure.Move;
import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Knight;

public class KnightTest {
	IBoard board = new Board(10);
	int x = 0,  y = 3;
	IPiece knight = new Knight(PieceColor.WHITE);
	Square sq = board.getSquare(0, 0);

	@Before
	public void setUp() throws Exception {
		sq.putPiece(knight);
	}

	@Test
	public void canPlaceInBoard() {
		assertEquals(sq.getPiece(), knight);
	}

	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Move> legalKnightSquares = sq.getPiece().getLegalMoves(sq, board);
		for (int i = 0; i < legalKnightSquares.size(); i++) {
			if (!board.withinBoard(legalKnightSquares.get(i).getTo()))
				fail("should not move outside board");
		}
	}
	
	@Test
	public void cantMoveDiagonally() {
		ArrayList<Move> legalKnightSquares = sq.getPiece().getLegalMoves(sq, board);
		for (Move m : legalKnightSquares) {
			if (m.getTo().equals(board.getSquare(1, 1))) {
				fail("Can not move diagonally");
			}
		}
	}
	
	@Test
	public void cantMoveHorisontally() {
		ArrayList<Move> legalKnightSquares = sq.getPiece().getLegalMoves(sq, board);
		for (Move m : legalKnightSquares) {
			if (m.getTo().equals(board.getSquare(0, 2))) {
				fail("Can not move horizontally");
			}
		}
	}
	
	@Test
	public void canMakeLegalMove() {
		ArrayList<Move> legalKnightSquares = sq.getPiece().getLegalMoves(sq, board);
		for (Move m : legalKnightSquares) {
			if (m.getTo().equals(board.getSquare(1, 2))) {
				return;
			}
		}
		fail("Should be able to make a legal move!");
	}
	@Test
	public void canFindLegalPositions() {
		assertTrue(sq.getPiece().getLegalMoves(sq, board).size() > 0);
	}
}
