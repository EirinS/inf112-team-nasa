package tests.boardstructureTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Rook;

public class MoveTest {
	IPiece rook = new Rook(PieceColor.BLACK);
	Square to = new Square(0, 0);
	Square from = new Square(2, 2);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void equalMovesAreEqual() {
		Move m = new Move(to, from, rook, null, MoveType.REGULAR);
		Move m1 = new Move(to, from, rook, null, MoveType.REGULAR);
		assertEquals(m, m1);
	}

}
