package tests.pieceClassesTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

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
		ArrayList<Square> legalKnightSquares = sq.getPiece().legalPositions(sq, board);
		for (int i = 0; i < legalKnightSquares.size(); i++) {
			if (!board.withinBoard(legalKnightSquares.get(i)))
				fail("should not move outside board");
		}
	}
	
	@Test
	public void cantMoveDiagonally() {
		ArrayList<Square> legalKnightSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(!legalKnightSquares.contains(board.getSquare(1, 1)));		
	}
	
	@Test
	public void cantMoveHorisontally() {
		ArrayList<Square> legalKnightSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(!legalKnightSquares.contains(board.getSquare(0, 2)));		
	}
	
	@Test
	public void canMakeLegalMove() {
		ArrayList<Square> legalKnightSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(legalKnightSquares.contains(board.getSquare(1, 2)));		
	}
	@Test
	public void canFindLegalPositions() {
		assertTrue(sq.getPiece().legalPositions(sq, board).size() > 0);
	}
}
