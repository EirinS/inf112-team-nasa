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
import pieces.pieceClasses.Pawn;

public class PawnTest {
	IBoard board = new Board(8, PieceColor.WHITE);
	IPiece whitePawn = new Pawn(PieceColor.WHITE);
	Square sq = board.getSquare(3,6);
	
	@Before
	public void setUp() {
		sq.putPiece(whitePawn);
	}
	
	@Test
	public void pawnAppearsCorrectlyOnTheBoard() {
		assertEquals(whitePawn, sq.getPiece());
	}
	
	@Test
	public void pawnCanCaptureOpponentToTheEast() {
		IPiece opponentPawn = new Pawn(PieceColor.BLACK);
		Square opponentSq = board.getSquare(4,5);
		opponentSq.putPiece(opponentPawn);
		ArrayList<Move> moves = whitePawn.getLegalMoves(sq, board, PieceColor.WHITE);
		
		boolean canCaptureBlack = false;
		for (Move m : moves)
			if (m.getTo().equals(opponentSq)) canCaptureBlack = true;
		assertTrue(canCaptureBlack);
	}
	
	@Test
	public void pawnCanCaptureOpponentToTheWest() {
		IPiece opponentPawn = new Pawn(PieceColor.BLACK);
		Square opponentSq = board.getSquare(2,5);
		opponentSq.putPiece(opponentPawn);
		ArrayList<Move> moves = whitePawn.getLegalMoves(sq, board, PieceColor.WHITE);
		
		boolean canCaptureBlack = false;
		for (Move m : moves)
			if (m.getTo().equals(opponentSq)) canCaptureBlack = true;
		assert(canCaptureBlack);
	}
	
	@Test
	public void pawnCannotMoveDiagonallyWithoutEnemiesPresent() {
		ArrayList<Move> moves = whitePawn.getLegalMoves(sq, board, PieceColor.WHITE);
		for (Move m : moves)
			if (m.getTo().getX() != sq.getX())
				fail("Pawn should not be able to move diagonally without the presence on enemies there");
	}
	
	
	@Test
	public void pawnCannotMoveThroughOtherPieces() {
		IPiece otherPawn = new Pawn(PieceColor.WHITE);
		Square otherSq = board.getSquare(3,5);
		otherSq.putPiece(otherPawn);
		ArrayList<Move> moves = whitePawn.getLegalMoves(sq, board, PieceColor.WHITE);
		boolean canMoveAhead = false;
		for (Move m : moves) {
			if (m.getTo().equals(otherSq)) canMoveAhead = true;
		}
		assertFalse(canMoveAhead);
	}
	
	@Test
	public void pawnCannotMoveTwoSquaresIfItHasMovedPreviously() {
		//TODO must implement hasMoved logic properly in Pawn first
	}
	
	@Test
	public void pawnCanOnlyMoveForwardsWhenNotCapturing() {
		ArrayList<Move> moves = whitePawn.getLegalMoves(sq, board, PieceColor.WHITE);
		for (Move m : moves) {
			if (m.getTo().getX() != sq.getX())
				fail("A pawn that does not threaten any opponent pieces should"
						+ "either have no legal moves or only be able to move forward");
		}
	}
	
	@Test
	public void playerOnePawnMovesNorth() {
		//TODO
	}
	
	
	@Test
	public void playerTwoPawnMovesSouth() {
		//TODO
	}
}
