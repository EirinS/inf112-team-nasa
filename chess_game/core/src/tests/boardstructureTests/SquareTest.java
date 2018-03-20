package tests.boardstructureTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Rook;

public class SquareTest {
	
	private int x = 2, y = 1;
	//creates empty square
	private Square sq = new Square(x, y);
	private IPiece piece = new Rook(PieceColor.BLACK);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void emptySquareIsEmpty() {
		assertTrue(sq.isEmpty());
	}
	
	@Test
	public void nonEmptySquareIsNotEmpty() {
		sq.putPiece(piece);
		assertFalse(sq.isEmpty());
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
		sq.putPiece(piece);
		assertEquals(piece, sq.movePiece());
	}
	
	@Test
	public void blackSquareReturnsFalseForSquareIsWhite() {
		assertFalse(sq.squareIsWhite());
	}
	
	@Test
	public void whiteSquareReturnsTrueForSquareIsWhite() {
		Square sq = new Square(0,0);
		assertTrue(sq.squareIsWhite());
	}
	
	@Test
	public void movePieceLeavesSquareEmpty() {
		sq.putPiece(piece);
		assertFalse(sq.isEmpty());
		sq.movePiece();
		assertTrue(sq.isEmpty());
	}
	
	@Test
	public void putPieceGivesSamePieceOnGetPiece() {
		sq.putPiece(piece);
		assertEquals(piece, sq.getPiece());
	}
	
	@Test
	public void cannotPutPieceInSquareThatContainsPiece() {
		sq.putPiece(piece);
		boolean caughtException = false;
		try {
			sq.putPiece(new Rook(PieceColor.BLACK));			
		} catch (Exception e) {
			caughtException = true;
		}
		assertTrue(caughtException);
	}
	
	
	private void setupTakePiece() {
		sq.putPiece(piece);
		sq.takePiece();
	}
	
	@Test
	public void takePieceMovesPieceOutOfPlay() {
		setupTakePiece();
		assertFalse(piece.isInPlay());
	}
	
	@Test
	public void takePieceLeavesSquareEmpty() {
		setupTakePiece();
		assertTrue(sq.isEmpty());
	}
	
	@Test
	public void cannotPutNullPiece() {
		boolean thrown = false;
		try {
			sq.putPiece(null);
		} catch(Exception e) {
			thrown = true;
		}
		assertTrue(thrown);		
	}
	
	

}
