package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Rook;

public class RookTest {
	IBoard board = new Board(10, PieceColor.WHITE);
	IPiece rook = new Rook(PieceColor.WHITE);
	Square sq = board.getSquare(0,0);
	
	//board for castling tests
	IBoard cb = new Board(8, PieceColor.WHITE);
	//pieces for castling tests
	King k = new King(PieceColor.WHITE);
	Rook r = new Rook(PieceColor.WHITE);
	Rook r2 = new Rook(PieceColor.WHITE);

	@Before
	public void setUp() throws Exception {
		sq.putPiece(rook);
	}
	
	
	@Test
	public void canFindEnemies() {
		int x = 2, y = 3;
		board.getSquare(x, y).putPiece(rook);
		board.getSquare(x+1, y).putPiece(new Rook(PieceColor.BLACK));
		board.getSquare(x, y+1).putPiece(new King(PieceColor.BLACK));
		assertEquals(board.getSquare(x, y).getPiece().enemyPiecesReached(x, y, board, PieceColor.BLACK).size(), 2);
	}
	
	
	//----------------------------------------------------------------------------------------------
	private void setUpForCastlingTests() {
		cb.getSquare(4, 7).putPiece(k);
		cb.getSquare(0, 7).putPiece(r);
		cb.getSquare(7, 7).putPiece(r2);
	}
	
	@Test
	public void canFindRookMoveQueenSide() {
		setUpForCastlingTests();
		Square sq = cb.getSquare(0, 7);
		Square expectedSq = board.getSquare(sq.getX()+3, sq.getY());
		Move expected = new Move(sq, expectedSq, r, null, MoveType.QUEENSIDECASTLING);
		assertEquals(expected, r.castling(sq, cb));		
	}
	
	@Test
	public void canFindRookMoveKingSide() {
		setUpForCastlingTests();
		Square sq = cb.getSquare(7, 7);
		Square expectedSq = board.getSquare(sq.getX()-2, sq.getY());
		Move expected = new Move(sq, expectedSq, r2, null, MoveType.KINGSIDECASTLING);
		assertEquals(expected, r2.castling(sq, cb));		
	}
	
	@Test
	public void RookMoveCastlingMethodSetsBoardAsExpectedForQueenSideCastling() {
		setUpForCastlingTests();
		Square sq = cb.getSquare(0, 7);
		Square next = cb.getSquare(3, 7);
		Move m = r.castling(sq, cb);
		r.moveCastling(m.getFrom(), m.getTo(), m.getMoveType(), cb);
		assertTrue(sq.isEmpty());
		assertEquals(k, cb.getSquare(2, 7).getPiece());
		assertEquals(r, next.getPiece());
	
	}
	
	@Test
	public void rookMoveCastlingMethodSetsBoardAsExpectedForKingSideCastling() {
		setUpForCastlingTests();
		Square sq = cb.getSquare(7, 7);
		Square next = cb.getSquare(5, 7);
		Move m = r2.castling(sq, cb);
		r2.moveCastling(m.getFrom(), m.getTo(), m.getMoveType(), cb);
		assertTrue(sq.isEmpty());
		assertEquals(k, cb.getSquare(6, 7).getPiece());
		assertEquals(r2, next.getPiece());
	}
	
	//-----------------------------------------------------------------------------------------------
	
	
	@Test
	public void canFindLegalMovesInCorner() {
		board = new Board(5, PieceColor.WHITE);
		board.getSquare(0, 0).putPiece(rook);
		ArrayList<Move> moves = rook.getLegalMoves(board.getSquare(0, 0), board, PieceColor.WHITE);
		assertEquals(8, moves.size());
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
		for(Move m : ((Rook) rook).allFreeMoves(sq.getX(), sq.getY(), board, null)) {
			if (m.getTo().getX() != 0) {
				hasMovedTo = true;
			}
		}
		assertTrue(hasMovedTo);		
	}
	
	@Test
	public void canMoveVertical() {
		boolean hasMovedTo = false;
		for(Move m : ((Rook) rook).allFreeMoves(sq.getX(), sq.getY(), board, null)) {
			if (m.getTo().getY() != 0) {
				hasMovedTo = true;
			}
		}
		assertTrue(hasMovedTo);		
	}
	
	@Test
	public void cantMoveDiagonal() {
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
		for(int i = 0; i < legalRookMoves.size(); i++) {
			assertFalse(legalRookMoves.get(i).getTo() == board.getSquare(1, 1));
		}	
	}
	
	
	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
		for(int i = 0; i < legalRookMoves.size(); i++) {
			if(!board.withinBoard(legalRookMoves.get(i).getTo()))
					fail ("should not move outside board");
		}
	}
	
	
	@Test
	public void cantMoveBehindPieces() {
		board.getSquare(0, 2).putPiece(new Rook(PieceColor.WHITE));
		board.getSquare(2, 0).putPiece(new Rook(PieceColor.WHITE));
		
		ArrayList<Move> legalRookMoves = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
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
		ArrayList<Move> lst = rook.getLegalMoves(board.getSquare(0, 0), board, PieceColor.WHITE);
		for(int i = 0; i < lst.size(); i++) {
			if (lst.get(i).getTo().getPiece().getColor() == rook.getColor()) {
				fail("Should have different colors on pieces you can move to");
			}
		}
	}
	
	
}
