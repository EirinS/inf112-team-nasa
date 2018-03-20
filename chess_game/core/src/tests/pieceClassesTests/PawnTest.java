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
import pieces.pieceClasses.Pawn;

import static pieces.PieceColor.WHITE;
import static pieces.PieceColor.BLACK;

public class PawnTest {
	PieceColor playerOne = WHITE;
	PieceColor playerTwo = BLACK;
	IBoard board = new Board(8, playerOne);
	IPiece whitePawn = new Pawn(playerOne);
	Square sq = board.getSquare(3,6);
	
	@Before
	public void setUp() {
		sq.putPiece(whitePawn);
	}
	
	@Test
	public void blackPawnOnRowOneCanFindPromotion() {
		IBoard board = new Board(8, playerTwo);
		Pawn p = new Pawn(playerTwo);
		Square sq = board.getSquare(0, 1);
		sq.putPiece(p);
		
		for(Move m : p.getLegalMoves(sq, board, playerTwo))
			if (m.getMoveType() == MoveType.PROMOTION)
				return;
		fail("No promotion move found.");		
	}
	
	@Test
	public void whitwPawnOnRowSixCanFindPromotion() {
		IBoard board = new Board(8, playerOne);
		Pawn p = new Pawn(playerOne);
		Square sq = board.getSquare(0, 6);
		sq.putPiece(p);
		
		for(Move m : p.getLegalMoves(sq, board, playerTwo))
			if (m.getMoveType() == MoveType.PROMOTION)
				return;		
		fail("No promotion move found.");
	}
	
	@Test
	public void pawnAppearsCorrectlyOnTheBoard() {
		assertEquals(whitePawn, sq.getPiece());
	}
	
	@Test
	public void pawnCanCaptureOpponentToTheEast() {
		IPiece opponentPawn = new Pawn(playerTwo);
		Square opponentSq = board.getSquare(4,5);
		opponentSq.putPiece(opponentPawn);
		boolean canCaptureBlack = false;
		if (whitePawn.getLegalMoves(sq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().equals(opponentSq)))
			canCaptureBlack = true;
		assert(canCaptureBlack);
	}
	
	@Test
	public void pawnCanCaptureOpponentToTheWest() {
		IPiece opponentPawn = new Pawn(playerTwo);
		Square opponentSq = board.getSquare(2,5);
		opponentSq.putPiece(opponentPawn);
		boolean canCaptureBlack = false;
		if (whitePawn.getLegalMoves(sq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().equals(opponentSq)))
			canCaptureBlack = true;
		assert(canCaptureBlack);
	}
	
	@Test
	public void pawnCannotMoveDiagonallyWithoutEnemiesPresent() {
		if (whitePawn.getLegalMoves(sq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().getX() != sq.getX()))
			fail("Pawn should not be able to move diagonally without the presence of enemies"
					+ "on the immidiate diagonal squares");
	}
	
	@Test
	public void pawnCannotMoveThroughOtherPieces() {
		IPiece otherPawn = new Pawn(playerOne);
		Square otherSq = board.getSquare(3,5);
		otherSq.putPiece(otherPawn);
		if (whitePawn.getLegalMoves(sq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().equals(otherSq)))
			fail("A pawn should not be able to move to the square ahead if"
					+ " it is occupied by another piece of the same color");
	}
	
	@Test
	public void pawnCanOnlyMoveForwardsWhenNotCapturing() {
		if (whitePawn.getLegalMoves(sq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().getX() != sq.getX()))
			fail("A pawn that does not threaten any opponents should"
					+ " either have no legal moves or only be able to move forward");
	}
	
	@Test
	public void playerOnePawnMovesNorth() {
		IPiece playerOnePawn = new Pawn(playerOne);
		Square playerOneSq = board.getSquare(1,5);
		playerOneSq.putPiece(playerOnePawn);	
		if (playerOnePawn.getLegalMoves(playerOneSq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().getY() >= playerOneSq.getY()))
			fail("Pawns belonging to player one should move north");
	}
	
	@Test
	public void playerTwoPawnMovesSouth() {
		IPiece playerTwoPawn = new Pawn(playerTwo);
		Square playerTwoSq = board.getSquare(1,1);
		playerTwoSq.putPiece(playerTwoPawn);
		if (playerTwoPawn.getLegalMoves(playerTwoSq, board, playerOne)
				.stream().anyMatch(m -> m.getTo().getY() <= playerTwoSq.getY()))
			fail("Pawns belonging to player two should move south");
	}
}
