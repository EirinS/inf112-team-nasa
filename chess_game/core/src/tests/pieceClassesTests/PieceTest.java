package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Rook;

public class PieceTest {
	IBoard board = new Board(8, PieceColor.WHITE);
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
		assertTrue(board.getSquare(x, y).getPiece().getLegalMoves(board.getSquare(x, y), board, PieceColor.WHITE).size() > 0);
	}
	
	@Test
	public void legalPositionDoesNotChangeHasMovedFieldVariable() {
		rook.getLegalMoves(board.getSquare(x, y), board, PieceColor.WHITE);
		assertFalse(rook.hasMoved());
	}
	
	@Test
	public void moveSetsMoveFieldToTrue() {
		board.getSquare(x, y).movePiece();
		assertTrue(rook.hasMoved());
	}
	
	@Test
	public void removesPositionsInCheckFromValidPositonsHorizontal() {
		//rook is not in this pos without moving.
		rook.pieceMoved();
		IBoard newBoard = new Board(5, PieceColor.WHITE);
		newBoard.getSquare(0, 0).putPiece(enemyRook);
		newBoard.getSquare(0, 2).putPiece(rook);
		newBoard.getSquare(0, 3).putPiece(new King(PieceColor.WHITE));
		Square sq = newBoard.getSquare(0, 2);
		ArrayList<Move> leg = rook.getLegalMoves(sq, newBoard, PieceColor.WHITE);
		//legal position while capturing black, and before black.
		assertEquals(2, leg.size());
	}
	
	@Test
	public void removesPositionsInCheckFromValidPositonsvertical() {
		IBoard newBoard = new Board(8, PieceColor.WHITE);
		newBoard.getSquare(1, 0).putPiece(enemyRook);
		newBoard.getSquare(2, 0).putPiece(rook);
		
		//has legal moves before placing king
		assertTrue(rook.getLegalMoves((newBoard.getSquare(2, 0)), newBoard, PieceColor.WHITE).size() > 0);
		
		newBoard.getSquare(3, 0).putPiece(new King(PieceColor.WHITE));
		Square sq = newBoard.getSquare(2, 0);
		
		ArrayList<Move> leg = rook.getLegalMoves(sq, newBoard, PieceColor.WHITE);
		//legal positions should now become 1, because your king is always reachable by enemyRook if you move, but you can capture enemy rook.
		assertEquals(1, leg.size());
	}
	
	@Test
	public void removesPositionsInCheckFromValidPositonsverticalDoesNotChangePosition() {
		IBoard newBoard = new Board(8, PieceColor.WHITE);
		newBoard.getSquare(1, 0).putPiece(enemyRook);
		newBoard.getSquare(2, 0).putPiece(rook);		
		newBoard.getSquare(3, 0).putPiece(new King(PieceColor.WHITE));
		rook.getLegalMoves((newBoard.getSquare(2, 0)), newBoard, PieceColor.WHITE);
		assertEquals(rook, newBoard.getSquare(2, 0).getPiece());
		assertEquals(enemyRook, newBoard.getSquare(1, 0).getPiece());
	}
	
	@Test
	public void threatensKingIsTrueWhenThreatensKing() {
		ArrayList<IPiece> threatened = new ArrayList<IPiece>();
		threatened.add(new King(PieceColor.BLACK));
		assertTrue(((AbstractPiece) rook).threatensKing(threatened));
	}
	
	@Test
	public void canCapturePiece() {
		IPiece blackrook = new Rook(PieceColor.BLACK);
		board.getSquare(0, 0).putPiece(blackrook);
		blackrook.captureEnemyPieceAndMovePiece(board.getSquare(x, y), board.getSquare(0, 0));
		assertFalse(blackrook.isInPlay());
		assertEquals(rook, board.getSquare(0, 0).getPiece());
	}
}
