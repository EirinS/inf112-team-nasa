package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import boardstructure.Square;

public class SquareTest {
	
	private int x = 2, y = 1;
	//creates empty square
	private Square sq = new Square(x, y);
	//private IPiece piece = //TODO:create piece

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void emptySquareIsEmpty() {
		assertTrue(sq.isEmpty());
	}
	
	@Test
	public void nonEmptySquareIsNotEmpty() {
		//TODO: code
	}
	
	@Test
	public void xValueSameAsInitialization() {
		assertEquals(sq.getX(), x);
	}
	
	@Test
	public void yValueSameAsInitialization() {
		assertEquals(sq.getY(), y);
	}
	
	@Test
	public void removePieceReturnsSamePieceAsPlaced() {
		//sq.putPiece(piece);
		//assertEquals(piece, sq.removePiece());
	}
	
	//@Test
	public void movePieceLeavesSquareEmpty() {
		//sq.putPiece(piece);
		sq.movePiece();
		assertTrue(sq.isEmpty());
		//TODO: Needs piece to test
	}
	
	//@Test
	public void putPieceGivesSamePiece() {
		//sq.putPiece(piece);
		//assertEquals(piece, sq.getPiece());
		//TODO: Needs piece to test
	}
	
	@Test
	public void cannotPutPieceInSquareThatContainsPiece() {
		//TODO: code
	}
	
	@Test
	public void takePieceMovesPieceOutOfPlay() {
		//TODO: code
	}
	
	

}
