package tests.pieceClassesTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;

public class BishopsTest {
	
	IBoard board = new Board(8);
	Bishop wBishop = new Bishop(PieceColor.WHITE);
	int x = 3, y = 1;
	Square square = board.getSquare(x, y);

	@Before
	public void setUp () throws Exception {
		board.getSquare(x, y).putPiece(wBishop);
	}
	
	
	
	@Test 
	public void pieceIsInitialized(){
		assertEquals(board.getSquare(x, y).getPiece(), wBishop);
	}
	
}
