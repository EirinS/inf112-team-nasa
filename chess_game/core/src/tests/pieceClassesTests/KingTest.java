package tests.pieceClassesTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;

public class KingTest {
	IBoard board = new Board(10);
	King king = new King(PieceColor.WHITE);
	int x = 5, y = 5;
	Square sq = board.getSquare(x, y);
	

	@Before
	public void setUp() throws Exception {
		sq.putPiece(king);
	}

	@Test
	public void finds8validpositionsInEmptyBoard() {
		assertEquals(8, king.reachable(x,y, board).size());
	}

}
