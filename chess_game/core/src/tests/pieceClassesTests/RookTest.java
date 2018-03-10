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
import pieces.pieceClasses.King;
import pieces.pieceClasses.Rook;

public class RookTest {
	IBoard board = new Board(10);
	IPiece rook = new Rook(PieceColor.WHITE);
	Square sq = board.getSquare(0,0);

	@Before
	public void setUp() throws Exception {
		sq.putPiece(rook);
	}
	
	@Test
	public void testForGetMovable() {
		board = new Board(2);
		board.getSquare(0, 0).putPiece(rook);
		ArrayList<Square> moves = rook.getMovableSquares(0, 0, board);
		assertEquals(2, moves.size());
		assertTrue(moves.contains(board.getSquare(0, 1)));
		assertTrue(moves.contains(board.getSquare(1, 0)));
		
		//not itself
		assertFalse(moves.contains(board.getSquare(0, 0)));
	}
	
	@Test
	public void canFindEnemies() {
		int x = 0, y = 3;
		board.getSquare(x, y).putPiece(rook);
		board.getSquare(x+1, y).putPiece(new Rook(PieceColor.BLACK));
		board.getSquare(x, y+2).putPiece(new King(PieceColor.BLACK));
		assertEquals(board.getSquare(x, y).getPiece().enemyPiecesReached(x, y, board, PieceColor.BLACK).size(), 2);
	}
	
	@Test
	public void getTheSameRookWhenGettingRookFromSquarePutInto() {
		Square sq = new Square(0,0, rook);
		assertEquals(sq.getPiece(), rook);
	}

	@Test
	public void canPlaceInBoard() {
		assertEquals(sq.getPiece(), rook);
	}
	
	@Test
	public void cantMoveDiagonal() {
		ArrayList<Square> legalRookSquares = sq.getPiece().legalPositions(sq, board);
		assertTrue(!legalRookSquares.contains(board.getSquare(1, 1)));		
	}
	
	
	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Square> legalRookSquares = sq.getPiece().legalPositions(sq, board);
		for(int i = 0; i < legalRookSquares.size(); i++) {
			if(!board.withinBoard(legalRookSquares.get(i)))
					fail ("should not move outside board");
		}
	}
	
	@Test
	public void cantMoveBehindPieces() {
		board.getSquare(0, 2).putPiece(new Rook(PieceColor.WHITE));
		assertTrue(!sq.getPiece().legalPositions(sq, board).contains(board.getSquare(0, 3)));
		assertTrue(!sq.getPiece().legalPositions(sq, board).contains(board.getSquare(0, 4)));
		
		board.getSquare(2, 0).putPiece(new Rook(PieceColor.WHITE));
		assertTrue(!sq.getPiece().legalPositions(sq, board).contains(board.getSquare(3, 0)));
		assertTrue(!sq.getPiece().legalPositions(sq, board).contains(board.getSquare(4, 0)));
	}
	
}
