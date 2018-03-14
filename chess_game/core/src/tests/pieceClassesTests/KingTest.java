package tests.pieceClassesTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
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
	public void kingCanMoveUp() {
		int newy = this.y+1;
		boolean movedUp = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == x){
				movedUp = true;
			}
		}
		assertTrue(movedUp);
	}
	
	@Test
	public void kingCanMoveDown() {
		int newy = this.y-1;
		boolean movedDown = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == x){
				movedDown = true;
			}
		}
		assertTrue(movedDown);
	}
	
	@Test
	public void kingCanMoveLeft() {
		int newx = this.x+1;
		boolean movedUp = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == y && m.getTo().getX() == newx){
				movedUp = true;
			}
		}
		assertTrue(movedUp);
	}
	
	@Test
	public void kingCanMoveRight() {
		int newx = this.x-1;
		boolean movedDown = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == y && m.getTo().getX() == newx){
				movedDown = true;
			}
		}
		assertTrue(movedDown);
	}
	
	@Test
	public void kingCanMoveRightDiagonalUp() {
		int newy = this.y+1;
		int newx = this.x+1;
		boolean movedSide = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == newx){
				movedSide = true;
			}
		}
		assertTrue(movedSide);
	}
	
	@Test
	public void kingCanMoveLeftDiagonalUp() {
		int newy = this.y+1;
		int newx = this.x-1;
		boolean moved = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == newx){
				moved = true;
			}
		}
		assertTrue(moved);
	}
	
	@Test
	public void kingCanMoveLeftDiagonalDown() {
		int newy = this.y-1;
		int newx = this.x-1;
		boolean moved = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == newx){
				moved = true;
			}
		}
		assertTrue(moved);
	}
	
	@Test
	public void kingCanMoveRightDiagonalDown() {
		int newy = this.y-1;
		int newx = this.x+1;
		boolean moved = false;
		for (Move m : king.reachable(x, y, sq, board)) {
			if (m.getTo().getY() == newy && m.getTo().getX() == newx){
				moved = true;
			}
		}
		assertTrue(moved);
	}

	@Test
	public void finds8validpositionsInEmptyBoard() {
		assertEquals(8, king.reachable(x,y, sq, board).size());
	}

}
