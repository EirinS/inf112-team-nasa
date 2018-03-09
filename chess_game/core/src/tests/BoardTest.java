package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;

public class BoardTest {
	private int dim = 5;
	private IBoard board = new Board(dim);

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void cantCreateIllegalBoard() {
		boolean thrown = false;
		try {
			IBoard board = new Board(-1);
		} catch (Exception e){
			thrown = true;
		}
		assertTrue(thrown);
	}

	@Test
	public void checkHeightOfBoard() {
		assertEquals(board.getHeight(), dim);
	}
	
	@Test
	public void checkWidthOfBoard() {
		assertEquals(board.getWidth(), dim);
	}
	
	@Test
	public void checkIfBoardIsSquare() {
		assertEquals(board.getWidth(), board.getHeight());
	}
	
	@Test
	public void boardHasNoNullSpots() {
		assertFalse(board.getBoard().contains(null));		
	}
	
	@Test
	public void returnsCorrectSquare() {
	}
	
	@Test
	public void illegalMoveToLargerThanHeight() {
		assertFalse(board.withinBoard(new Square(dim,dim-1)));
	}
	
	@Test
	public void illegalMoveToLargerThanWidth() {
		assertFalse(board.withinBoard(new Square(dim-1,dim)));
	}
	
	@Test
	public void illegalMoveToNegativeHeight() {
		assertFalse(board.withinBoard(new Square(-1,0)));
	}
	
	@Test
	public void illegalMoveToNegativeWidth() {
		assertFalse(board.withinBoard(new Square(0,-1)));
	}
	
	@Test
	public void legalMoveToFreeSquare() {
		assertTrue(board.withinBoard(new Square(0, 0)));
	}
	
	@Test
	public void illegalMoveToTakenSquare() {
	}

	
	

}
