package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Queen;

public class QueenTest {
	IBoard board = new Board(8);
	IPiece queen = new Queen(PieceColor.WHITE);
	Square sq = board.getSquare(0, 0);
	
	@Before
	public void setUp() throws Exception {
		
		sq.putPiece(queen);
	}

	@Test
	public void canPlaceInBoard() {
		assertEquals(sq.getPiece(), queen);
	}

	@Test
	public void canMoveDiagonally() {
		ArrayList<Square> legalQueenSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(legalQueenSquares.contains(board.getSquare(1, 1)));	
	}
	
	@Test
	public void canMoveHorisontally() {
		ArrayList<Square> legalQueenSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(legalQueenSquares.contains(board.getSquare(1, 0)));	
	}
	
	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Square> legalQueenSquares = sq.getPiece().legalPositions(sq, board);
		for (int i = 0; i < legalQueenSquares.size(); i++) {
			if (!board.withinBoard(legalQueenSquares.get(i)))
				fail("should not move outside board");
		}
	}
}
