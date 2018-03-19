package tests.AITests;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;

import java.util.ArrayList;
import java.util.List;

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
	}
	
	@Test
	public void testThatMediumAIWithOnlyARookDetectsAndMovesToFreeRook(){
		ws.putPiece(w);
		bs.putPiece(b);
		wks.putPiece(b);
		
		AIMedium ai = new AIMedium(PieceColor.WHITE);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertEquals(move.getMovingPiece().toString(), "R");
		org.junit.Assert.assertEquals(move.getTo().getX(), 0);
		org.junit.Assert.assertEquals(move.getTo().getY(), 2);
		org.junit.Assert.assertEquals(move.getFrom().getX(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getY(), 0);
	}
	
	@Test
	public void getPossibleBoardsTest() {
		AIMedium ai = new AIMedium(PieceColor.WHITE);
		List<Move> moves = board.getAvailableMoves(PieceColor.WHITE);
		ArrayList<Board> boards = ai.getPossibleBoards(board,moves);
		org.junit.Assert.assertEquals(boards.size(),moves.size());
		for(int i=0;i< boards.size();i++) {
			String a = moves.get(i).getFrom().getPiece().toString();
			String b = boards.get(i).getSquare(moves.get(i).getTo().getX(), moves.get(i).getTo().getY()).getPiece().toString();
			org.junit.Assert.assertEquals(a, b);
		}
	}
	
	@Test
	public void testThatMediumAIWithOnlyRooksDetectsTheBestMoveWhite(){
		ws.putPiece(w);
		bs.putPiece(b);
		wks.putPiece(b);
		br2.putPiece(b);
		br3.putPiece(w);

		AIMedium ai = new AIMedium(PieceColor.WHITE);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertEquals(move.getMovingPiece().toString(), "R");
		org.junit.Assert.assertEquals(move.getTo().getX(), 7);
		org.junit.Assert.assertEquals(move.getTo().getY(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getX(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getY(), 0);
	}
	@Test
	public void testThatMediumAIWithOnlyRooksDetectsTheBestMoveBlack(){
		ws.putPiece(b);
		bs.putPiece(w);
		wks.putPiece(w);
		br2.putPiece(w);
		br3.putPiece(b);

		AIMedium ai = new AIMedium(PieceColor.BLACK);
		Move move = ai.calculateMove(board);
		org.junit.Assert.assertEquals(move.getMovingPiece().toString(), "R");
		org.junit.Assert.assertEquals(move.getTo().getX(), 7);
		org.junit.Assert.assertEquals(move.getTo().getY(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getX(), 0);
		org.junit.Assert.assertEquals(move.getFrom().getY(), 0);
	}
	
}
