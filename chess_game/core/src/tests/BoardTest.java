package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.BoardColor;
import boardstructure.IBoard;
import boardstructure.Square;

public class BoardTest {
	private int height = 5, width = 5;
	private IBoard board = new Board(height, width);

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void cantCreateIllegalBoard() {
		boolean thrown = false;
		try {
			IBoard board = new Board(-1,-1);
		} catch (Exception e){
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	public void checkHeightOfBoard() {
		assertEquals(board.getHeight(), height);
	}
	
	@Test
	public void checkWidthOfBoard() {
		assertEquals(board.getWidth(), width);
	}
	
	@Test
	public void returnsCorrectSquare() {
		//TODO: code
	}
	
	@Test
	public void illegalMoveToLargerThanHeight() {
		assertFalse(board.moveable(new Square(height,width-1, BoardColor.WHITE)));
	}
	
	@Test
	public void illegalMoveToLargerThanWidth() {
		assertFalse(board.moveable(new Square(height-1,width, BoardColor.WHITE)));
	}
	
	@Test
	public void illegalMoveToNegativeHeight() {
		assertFalse(board.moveable(new Square(-1,0, BoardColor.WHITE)));
	}
	
	@Test
	public void illegalMoveToNegativeWidth() {
		assertFalse(board.moveable(new Square(0,-1, BoardColor.WHITE)));
	}
	
	@Test
	public void legalMoveToFreeSquare() {
		assertTrue(board.moveable(new Square(0, 0, BoardColor.WHITE)));
	}
	
	@Test
	public void illegalMoveToTakenSquare() {
		//TODO: code
	}

	
	

}
