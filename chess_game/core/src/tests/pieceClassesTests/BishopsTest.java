package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
//	@Test
//	public void printListOfSquares(){
//		ArrayList<Square> possibleMoves = wBishop.allReachableSquares(x, y, board);
//		int i = 0;
//		for (Square square : possibleMoves){
//			i++;
//			System.out.print(i +". ");
//			System.out.println("X: " + square.getX() + " Y: " + square.getY());
//		}		
//	}
}