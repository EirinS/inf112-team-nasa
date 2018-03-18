package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;

public class BishopsTest {
	
	IBoard board = new Board(8);
	Bishop wBishop = new Bishop(PieceColor.WHITE);
	int x = 4, y = 4;
	Square square = board.getSquare(x, y);

	@Before
	public void setUp () throws Exception {
		board.getSquare(x, y).putPiece(wBishop);
	}
	
	
	@Test 
	public void pieceIsInitialized(){
		assertEquals(square.getPiece(), wBishop);
	}
	

	@Test
	public void legalPositionsMoreThanZero() {
		assertTrue(square.getPiece().getLegalMoves(square, board, PieceColor.WHITE).size() > 0);
	}
	
	/**
	 * Currently: Something wrong with < > operators in method. Does not include
	 *  (8,8). If the y coordinate is set to an odd number, give "outofbounds" 
	 *  the test should still work if (7, 7) = (8, 8)
	 */

	@Test
	public void diagonalMove() {
		ArrayList<Move> move = square.getPiece().getLegalMoves(square, board, PieceColor.WHITE);
	//	int i = 0;
		boolean found = false;
		for (Move aMove : move) {
	//		i++;
	//		System.out.println(i + ". " + aMove.getTo() + " " + board.getSquare(7, 7));
			if (aMove.getTo().equals(board.getSquare(7, 7))){
				found = true;
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void cannotMoveHorisontally(){
		ArrayList<Move> move = square.getPiece().getLegalMoves(square, board, PieceColor.WHITE);
		for (Move aMove : move) {
			assertFalse(aMove.getTo().equals(board.getSquare(2, 4)));
		}
		
	}
	
	@Test 
	public void cannotMoveVertically(){
		ArrayList<Move> move = square.getPiece().getLegalMoves(square, board, PieceColor.WHITE);
		for (Move aMove : move) {
			assertFalse(aMove.getTo().equals(board.getSquare(4, 7)));
		}
	}
	
	@Test
	public void cannotShareSquareWithSameTeam(){
		Bishop wBishopTwo = new Bishop(PieceColor.WHITE);
		board.getSquare(6, 6).putPiece(wBishopTwo);
		ArrayList<Move> move = square.getPiece().getLegalMoves(square, board, PieceColor.WHITE);
		boolean found = false;
		for (Move aMove : move) {
			if (aMove.getTo().equals(board.getSquare(7, 7))){
				found = true;
			}
		}
		assertFalse(found);
		
	}
	@Test
	public void cannotMovePastPiece(){
		Bishop wBishopTwo = new Bishop(PieceColor.WHITE);
		board.getSquare(6, 6).putPiece(wBishopTwo);
		ArrayList<Move> move = square.getPiece().getLegalMoves(square, board, PieceColor.WHITE);
		boolean found = false;
		for (Move aMove : move) {
			if (aMove.getTo().equals(board.getSquare(6, 6))){
				found = true;
			}
		}
		assertFalse(found);
	}
}	