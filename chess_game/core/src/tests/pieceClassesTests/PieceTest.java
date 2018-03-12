package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Queen;
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
		ArrayList<Square> squares = board.getSquare(x, y).getPiece().getMovableSquares(x, y, board);
		for(int i = 0; i < squares.size(); i++) {
			assertTrue(squares.get(i).isEmpty());
		}
	}
	
	@Test
	public void moveSetsMoveFieldToTrue() {
		board.getSquare(x, y).movePiece();
		assertTrue(rook.hasMoved());
	}
	
	@Test
	//note to self: testen må gjøres om når man kan ta brikker.
	public void removesPositionsInCheckFromValidPositonsHorizontal() {
		IBoard newBoard = new Board(5);
		newBoard.getSquare(0, 0).putPiece(enemyRook);
		newBoard.getSquare(0, 1).putPiece(rook);
		newBoard.getSquare(0, 2).putPiece(new King(PieceColor.WHITE));
		Square sq = newBoard.getSquare(0, 1);
		ArrayList<Square> leg = rook.legalPositions(sq, newBoard);
		assertEquals(0, leg.size());
	}
	
	@Test
	//note to self: testen må gjøres om når man kan ta brikker.
	public void removesPositionsInCheckFromValidPositonsvertical() {
		IBoard newBoard = new Board(5);
		newBoard.getSquare(1, 0).putPiece(enemyRook);
		newBoard.getSquare(2, 0).putPiece(rook);
		
		//has legal moves before placing king
		assertTrue(rook.legalPositions((newBoard.getSquare(2, 0)), newBoard).size() > 0);
		
		newBoard.getSquare(3, 0).putPiece(new King(PieceColor.WHITE));
		Square sq = newBoard.getSquare(2, 0);
		
		ArrayList<Square> leg = rook.legalPositions(sq, newBoard);
		//legal positions should now become 0, because your king is always reachable by enemyRook if you move
		assertEquals(0, leg.size());
	}
	
	@Test
	public void removesPositionsInCheckFromValidPositonsverticalDoesNotChangePosition() {
		IBoard newBoard = new Board(5);
		newBoard.getSquare(1, 0).putPiece(enemyRook);
		newBoard.getSquare(2, 0).putPiece(rook);		
		newBoard.getSquare(3, 0).putPiece(new King(PieceColor.WHITE));
		assertEquals(rook, newBoard.getSquare(2, 0).getPiece());
		assertEquals(enemyRook, newBoard.getSquare(1, 0).getPiece());
	}
	
	
	
	@Test
	public void threatensKingIsTrueWhenThreatensKing() {
		ArrayList<IPiece> threatened = new ArrayList<IPiece>();
		threatened.add(new King(PieceColor.BLACK));
		assertTrue(((AbstractPiece) rook).threatensKing(threatened));
	}
	

}
