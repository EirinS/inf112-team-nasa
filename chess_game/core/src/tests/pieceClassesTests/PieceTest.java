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
import pieces.pieceClasses.Rook;

public class PieceTest {
	IBoard board = new Board(10);
	IPiece rook = new Rook(PieceColor.WHITE);
	int x = 0,  y = 3;
	IPiece enemyRook = new Rook(PieceColor.BLACK);
	
	@Before
	public void setUp() throws Exception {
		board.getSquare(x, y).putPiece(rook);
	}
	
	@Test
	public void pieceCanBeTakenAndIsNotInPlayAfter() {
		rook.takePiece();
		assertFalse(rook.isInPlay());
	}
	
	@Test
	public void canFindLegalPositions() {
		assertTrue(board.getSquare(x, y).getPiece().legalPositions(board.getSquare(x, y), board).size() > 0);
	}
	
	@Test
	public void canFindEmptySquares() {
		ArrayList<Square> squares = board.getSquare(x, y).getPiece().getEmptySquares(x, y, board);
		for(int i = 0; i < squares.size(); i++) {
			assertTrue(squares.get(i).isEmpty());
		}
	}
	
	@Test
	public void moveSetsMoveFieldToTrue() {
		board.getSquare(x, y).movePiece();
		assertTrue(rook.hasMoved());
	}

}
