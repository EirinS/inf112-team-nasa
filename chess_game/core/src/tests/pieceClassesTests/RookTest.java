package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
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
	public void canFindLegalMoves() {
		board = new Board(2);
		board.getSquare(0, 0).putPiece(rook);
		ArrayList<Move> moves = rook.getLegalMoves(board.getSquare(0, 0), board);
		assertEquals(2, moves.size());
	}
	
	@Test
	public void canFindEnemies() {
		int x = 2, y = 3;
		board.getSquare(x, y).putPiece(rook);
		board.getSquare(x+1, y).putPiece(new Rook(PieceColor.BLACK));
		board.getSquare(x, y+1).putPiece(new King(PieceColor.BLACK));
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
	public void canMoveHorizontal() {
		boolean hasMovedTo = false;
		for(Move m : ((Rook) rook).allFreeMoves(sq.getX(), sq.getY(), board)) {
			if (m.getTo().getX() != 0) {
				hasMovedTo = true;
			}
		}
		assertTrue(hasMovedTo);		
	}
	
	@Test
	public void canMoveVertical() {
		boolean hasMovedTo = false;
		for(Move m : ((Rook) rook).allFreeMoves(sq.getX(), sq.getY(), board)) {
			if (m.getTo().getY() != 0) {
				hasMovedTo = true;
			}
		}
		assertTrue(hasMovedTo);		
	}
	
	@Test
	public void cantMoveDiagonal() {
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board);
		for(int i = 0; i < legalRookMoves.size(); i++) {
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(1, 1));
		}	
	}
	
	
	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board);
		for(int i = 0; i < legalRookMoves.size(); i++) {
			if(!board.withinBoard(legalRookMoves.get(i).getTo()))
					fail ("should not move outside board");
		}
	}
	
	
	@Test
	public void cantMoveBehindPieces() {
		board.getSquare(0, 2).putPiece(new Rook(PieceColor.WHITE));
		board.getSquare(2, 0).putPiece(new Rook(PieceColor.WHITE));
		
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board);
		for(int i = 0; i < legalRookMoves.size(); i++) {
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(0, 3));
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(0, 4));
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(3, 0));
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(4, 0));
		}
	}
	
	
	@Test
	public void cantMoveToSquaresContainingSameColor() {
		board.getSquare(0, 1).putPiece(new Rook(PieceColor.WHITE));
		board.getSquare(1, 0).putPiece(new Rook(PieceColor.WHITE));
		ArrayList<Move> lst = rook.getLegalMoves(board.getSquare(0, 0), board);
		for(int i = 0; i < lst.size(); i++) {
			if (lst.get(i).getTo().getPiece().getColor() == rook.getColor()) {
				fail("Should have different colors on pieces you can move to");
			}
		}
	}
	
	
}
