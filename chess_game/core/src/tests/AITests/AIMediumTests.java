package tests.AITests;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import org.junit.Before;
import org.junit.Test;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Rook;
import player.AIEasy;
import player.AIMedium;

/**
 * Created by jonas on 17/03/2018.
 */
public class AIMediumTests {
	IBoard board = new Board(8, PieceColor.WHITE);
	Rook w = new Rook(PieceColor.WHITE);
	King wk = new King(PieceColor.WHITE);
	King bk = new King(PieceColor.BLACK);
	Rook b = new Rook(PieceColor.BLACK);
	Square bks = board.getSquare(2,4);
	Square wks = board.getSquare(6,4);
	Square ws = board.getSquare(0, 0);
	Square bs = board.getSquare(0, 2);
	Square br = board.getSquare(0, 3);
	Square br2 = board.getSquare (7,0);
	Square br3 = board.getSquare(3, 7);

	@Before
	public void setUp() throws Exception {
		ws.putPiece(w);
		bs.putPiece(b);
		wks.putPiece(b);
		//bs.putPiece(b);
		//br2.putPiece(b);
		//br3.putPiece(w);
		
		//wks.putPiece(wk);
		//bks.putPiece(bk);
	}
	//bugs need to be fixed before the tests are valid, they are not complete.
	@Test
	public void testThatMediumAIWithOnlyARookDetectsAndMovesToFreeRook(){
		AIMedium ai = new AIMedium(PieceColor.WHITE);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertEquals(move.getMovingPiece().toString(), "R");
		org.junit.Assert.assertEquals(move.getTo().getX(), 0);
		org.junit.Assert.assertEquals(move.getTo().getY(), 2);
		org.junit.Assert.assertEquals(move.getFrom().getX(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getY(), 0);
	}
	
	@Test
	public void testThatMediumAIWithOnlyARookDetectsTheBestMove(){
		
		AIMedium ai = new AIMedium(PieceColor.WHITE);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertEquals(move.getMovingPiece().toString(), "R");
		org.junit.Assert.assertEquals(move.getTo().getX(), 7);
		org.junit.Assert.assertEquals(move.getTo().getY(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getX(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getY(), 0);
	}
	
}
