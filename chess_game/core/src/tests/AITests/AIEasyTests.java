package tests.AITests;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import org.junit.Before;
import org.junit.Test;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import player.AIEasy;

/**
 * Created by jonas on 17/03/2018.
 */
public class AIEasyTests {
	IBoard board = new Board(8);
	King king = new King(PieceColor.WHITE);
	int x = 5, y = 5;
	Square sq = board.getSquare(x, y);


	@Before
	public void setUp() throws Exception {
		sq.putPiece(king);
	}

	@Test
	public void testThatEasyAIMovesFromItsOriginalPositionToAnotherOne(){
		AIEasy ai = new AIEasy(PieceColor.WHITE);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertTrue(move.getTo().getX() != 5 || move.getTo().getY() != 5);
	}
}
