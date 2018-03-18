package tests.pieceClassesTests;

import static org.junit.Assert.*;

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

public class KingTest {
	private IBoard board = new Board(8);
	private King king = new King(PieceColor.WHITE);
	private int x = 3, y = 3;
	private Square sq = board.getSquare(x, y);
	private IPiece rook = new Rook(PieceColor.WHITE);
	private IPiece sndRook = new Rook(PieceColor.WHITE);
	

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void threeLegalMovesInCornerWithoutCastling() {
		IBoard b = new Board(8);
		Square sq = b.getSquare(0, 0);
		sq.putPiece(king);
		assertEquals(3, king.getLegalMoves(sq, b, PieceColor.WHITE).size());
	}
	
	@Test
	public void canFind6LegalMovesWith1Castling() {
		IBoard b = new Board(8);
		Square sq = b.getSquare(4, 7);
		sq.putPiece(king);
		b.getSquare(7, 7).putPiece(rook);
		assertEquals(6, king.getLegalMoves(sq, b, PieceColor.WHITE).size());
	}
	
	@Test
	public void canFind7LegalMovesWith2Castlings() {
		IBoard b = new Board(8);
		Square sq = b.getSquare(4, 7);
		sq.putPiece(king);
		b.getSquare(7, 7).putPiece(rook);
		b.getSquare(0, 7).putPiece(new Rook(PieceColor.WHITE));
		assertEquals(7, king.getLegalMoves(sq, b, PieceColor.WHITE).size());
	}
	
	
	@Test
	public void kingsMoveCastlingMethodSetsBoardAsExpectedForQueenSideCastling() {
		IBoard board = new Board(8);
		board.getSquare(4, 7).putPiece(king);
		board.getSquare(0, 7).putPiece(rook);
		king.moveCastling(board.getSquare(4, 7), board.getSquare(2, 7), MoveType.QUEENSIDECASTLING, board);
		assertEquals(board.getSquare(2, 7).getPiece(), king);
		assertEquals(board.getSquare(3, 7).getPiece(), rook);
	}
	
	@Test
	public void kingsMoveCastlingMethodSetsBoardAsExpectedForKingSideCastling() {
		IBoard board = new Board(8);
		board.getSquare(4, 7).putPiece(king);
		board.getSquare(7, 7).putPiece(rook);
		king.moveCastling(board.getSquare(4, 7), board.getSquare(6, 7), MoveType.KINGSIDECASTLING, board);
		assertEquals(board.getSquare(6, 7).getPiece(), king);
		assertEquals(board.getSquare(5, 7).getPiece(), rook);
	}
		
	private void setUpCastlingTest() {
		board.getSquare(0, 7).putPiece(rook);
		board.getSquare(4, 7).putPiece(king);
	}
	
	@Test
	public void canFindFirstPiecesInHorizontalDirection() {
		setUpCastlingTest();
		Square sq = board.getSquare(4, 7);
		board.getSquare(7, 7).putPiece(sndRook);
		
		assertEquals(2, king.getFirstPieceHorizontal(sq, board).size());
		assertEquals(king.getFirstPieceHorizontal(sq, board).get(rook), board.getSquare(0, 7));
	}
	
	@Test
	public void cantMoveThroughPositionsInCheck() {
		setUpCastlingTest();
		IPiece enemyRook = new Rook(PieceColor.BLACK);
		//position king must move through for castling
		board.getSquare(3, 0).putPiece(enemyRook);
		assertTrue(king.kingMovesThroughCheckPos(board.getSquare(4, 7), board, false));
		
	}
	
	@Test
	public void noCastlingIfKingMoved() {
		setUpCastlingTest();
		king.pieceMoved();
		assertEquals(null, king.castling(board.getSquare(4, 7), board));
	}
	
	@Test
	public void noCastlingIfKingInCheck() {
		setUpCastlingTest();
		IPiece enemyRook = new Rook(PieceColor.BLACK);
		board.getSquare(4, 0).putPiece(enemyRook);
		assertEquals(null, king.castling(board.getSquare(4, 7), board));
	}
	
	@Test
	public void noCastlingIfRookMoved() {
		setUpCastlingTest();
		rook.pieceMoved();
		assertEquals(null, king.castling(board.getSquare(4, 7), board));
	}
	
	@Test
	public void canFindQueenSideCastling() {
		setUpCastlingTest();
		assertEquals(king.castling(board.getSquare(4, 7), board).size(), 1);
		
		Move expected = new Move(board.getSquare(4, 7), board.getSquare(2, 7), king, null, MoveType.QUEENSIDECASTLING);
		assertEquals(expected, king.castling(board.getSquare(4, 7), board).get(0));
	}
	
	@Test
	public void canFindKingSideCastling() {
		setUpCastlingTest();
		board.getSquare(0, 7).takePiece(); //remove queensidecastling-option
		board.getSquare(7, 7).putPiece(sndRook);
		assertEquals(king.castling(board.getSquare(4, 7), board).size(), 1);
		
		Move expected = new Move(board.getSquare(4, 7), board.getSquare(6, 7), king, null, MoveType.KINGSIDECASTLING);
		assertEquals(expected, king.castling(board.getSquare(4, 7), board).get(0));
	}
	
	@Test
	public void canFind6legalPositionsWhen1RookPossible() {
		setUpCastlingTest();
		assertEquals(6, king.getLegalMoves(board.getSquare(4, 7), board, PieceColor.WHITE).size());
	}
	
	@Test
	public void canFindPlayer1CastlingBlack() {
		IBoard newBoard = new Board(8);
		King king = new King(PieceColor.BLACK);
		Rook rook = new Rook(PieceColor.BLACK);
		newBoard.getSquare(4, 7).putPiece(king);
		newBoard.getSquare(0, 7).putPiece(rook);
		Move expected = new Move(newBoard.getSquare(4, 7), newBoard.getSquare(2, 7), king, null, MoveType.QUEENSIDECASTLING);
		assertEquals(expected, king.castling(newBoard.getSquare(4, 7), newBoard).get(0));	
	}
	
	@Test
	public void canFindPlayer2CastlingBlack() {
		IBoard newBoard = new Board(8);
		King king = new King(PieceColor.BLACK);
		Rook rook = new Rook(PieceColor.BLACK);
		newBoard.getSquare(4, 0).putPiece(king);
		newBoard.getSquare(0, 0).putPiece(rook);
		Move expected = new Move(newBoard.getSquare(4, 0), newBoard.getSquare(2, 0), king, null, MoveType.QUEENSIDECASTLING);
		assertEquals(expected, king.castling(newBoard.getSquare(4, 0), newBoard).get(0));	
	}
	
	private void setUpForMoveTests() {
		sq.putPiece(king);		
	}
	
	@Test
	public void kingCanMoveUp() {
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
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
		setUpForMoveTests();
		assertEquals(8, king.reachable(x,y, sq, board).size());
	}

}
