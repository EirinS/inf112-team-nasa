package tests.pieceClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import boardstructure.Move;
import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Queen;

public class QueenTest {
	IBoard board = new Board(8, PieceColor.WHITE);
	IPiece queen = new Queen(PieceColor.WHITE);
	Square sq = board.getSquare(0, 0);
	
	@Before
	public void setUp() throws Exception {
		sq.putPiece(queen);
	}

	@Test
	public void canPlaceInBoard() {
		assertEquals(sq.getPiece(), queen);
	}

	@Test
	public void canMoveDiagonally() {
		ArrayList<Move> legalQueenSquares = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
		for (Move m : legalQueenSquares) {
			if (m.getTo().equals(board.getSquare(1, 1))) {
				return;
			}
		}
		fail("Should be able to move diagonally!");
	}
	
	@Test
	public void canMoveHorisontally() {
		ArrayList<Move> legalQueenSquares = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
		for (Move m : legalQueenSquares) {
			if (m.getTo().equals(board.getSquare(1, 0))) {
				return;
			}
		}
		fail("Should be able to move horizontally!");
	}
	
	@Test
	public void illegalMoveOutsideBoard() {
		ArrayList<Move> legalQueenSquares = sq.getPiece().getLegalMoves(sq, board, PieceColor.WHITE);
		for (int i = 0; i < legalQueenSquares.size(); i++) {
			if (!board.withinBoard(legalQueenSquares.get(i).getTo()))
				fail("should not move outside board");
		}
	}
}
