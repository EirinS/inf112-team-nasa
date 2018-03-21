package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import game.chessGame.GameInfo;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import game.chessGame.ChessGame;
import game.chessGame.GameInfo;
import game.chessGame.IChessGame;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Pawn;
import pieces.pieceClasses.Queen;
import pieces.pieceClasses.Rook;

public class GameTest {
	IChessGame game = new ChessGame(new GameInfo(null, null, PieceColor.BLACK, null, null), null);
	IBoard board = new Board(8, PieceColor.WHITE);
	IBoard other = new Board(8, PieceColor.WHITE);

	@Before
	public void setUp() throws Exception {
		game.setBoard(board);
	}
	
	@Test
	public void twoDifferentBoardsAreDifferent() {
		IBoard current = new Board(8, PieceColor.WHITE);
		IBoard other = new Board(8, PieceColor.WHITE);
		other.getSquare(1, 1).putPiece(new Pawn(PieceColor.BLACK));
		other.getSquare(1, 0).putPiece(new Rook(PieceColor.BLACK));
		assertFalse(((ChessGame) game).isSame(current, other));
	}
	
	/*@Test
	public void twoDifferentBoardsAreDifferentWithIsSame() {
		ChessGame g = new ChessGame(new GameInfo(null, null, PieceColor.BLACK, null, null), null);
		ArrayList<Move> m = g.getSquares().getSquare(1, 0).getPiece().getLegalMoves(board.getSquare(1, 0), g.getBoard(), PieceColor.BLACK);
		g.getSquares().move(m.get(0).getFrom(), m.get(0).getTo());
		ChessGame g1 = new ChessGame(new GameInfo(null, null, PieceColor.BLACK, null, null), null);
		assertFalse(g.isSame(g.getSquares(), g1.getSquares()));
	}*/


	@Test
	public void containsWorksWithDifferentBoardsButEqualFieldVariablesForSquare() {
		IPiece rook = new Rook (PieceColor.WHITE);
		board.getSquare(1, 1).putPiece(rook);
		Square sq = other.getSquare(1, 1);
		sq.putPiece(rook);
		assertTrue(game.contains(board, sq));	
	}

	@Test
	public void containsFailsIfSquareIsNotInBoard() {
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.BLACK));
		assertFalse(game.contains(board, sq));	
	}

	@Test
	public void fiftyMoveRuleIffiftyMovesOnlyRook() {
		int moves = 50;
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(0, 0).putPiece(r);
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(0, 2);
		for(int i = 0; i < moves; i++) {
			if(i % 2 == 0)
				board.move(one, two, true);
			else 
				board.move(two, one, true);
		}
		assertEquals(moves, board.getHistory().size());
		assertTrue(game.fiftyMoves());
	}

	@Test
	public void notFiftyMoveRuleIfPawnMoves() {
		int moves = 50;
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(0, 0).putPiece(r);
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(0, 2);
		for(int i = 0; i < moves; i++) {
			if(i == 25) {
				Pawn p = new Pawn(PieceColor.WHITE);
				board.getSquare(5, 5).putPiece(p);
				board.move(board.getSquare(5, 5), board.getSquare(5, 4),true);
				board.move(two, one,true);
			}
			else {
				if(i % 2 == 0)
					board.move(one, two, true);
				else 
					board.move(two, one, true);
			}
		}
		//does two moves at 25
		assertEquals(moves+1, board.getHistory().size());
		assertFalse(game.fiftyMoves());
	}
	
	@Test
	public void canFindCheckMate() {
		King k = new King(PieceColor.WHITE);
		board.getSquare(3, 0).putPiece(k);
		board.getSquare(3, 1).putPiece(new Queen(PieceColor.BLACK));
		board.getSquare(4, 2).putPiece(new King(PieceColor.BLACK));
		assertTrue(game.checkmate());
	}
	
	@Test
	public void cantFindCheckMateIfNotCheckMateIfYouCantMoveOnOtherPlayersTurn() {
		King k = new King(PieceColor.BLACK);
		board.getSquare(3, 0).putPiece(k);
		board.getSquare(3, 1).putPiece(new Queen(PieceColor.WHITE));
		board.getSquare(4, 2).putPiece(new King(PieceColor.WHITE));
		assertFalse(game.checkmate());
	}
	
	private void impossibleCheckMateSetup() {
		board.getSquare(3, 3).putPiece(new King(PieceColor.WHITE));
		board.getSquare(5, 5).putPiece(new King(PieceColor.BLACK));
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKings() {
		impossibleCheckMateSetup();
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKingsOneBishopBlack() {
		impossibleCheckMateSetup();
		board.getSquare(6, 0).putPiece(new Bishop(PieceColor.BLACK));
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKingsOneBishopWhite() {
		impossibleCheckMateSetup();
		board.getSquare(6, 0).putPiece(new Bishop(PieceColor.WHITE));
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKingsOneKnightBlack() {
		impossibleCheckMateSetup();
		board.getSquare(6, 0).putPiece(new Knight(PieceColor.BLACK));
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKingsOneKnightWhite() {
		impossibleCheckMateSetup();
		board.getSquare(6, 0).putPiece(new Knight(PieceColor.WHITE));
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void impossibleCheckMateWhenTwoKingsTwoBishopsSameSquareColorDifferentPlayers() {
		impossibleCheckMateSetup();
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(2, 0);
		one.putPiece(new Bishop(PieceColor.WHITE));
		two.putPiece(new Bishop(PieceColor.BLACK));
		assertTrue(game.impossibleCheckmate());
	}
	
	@Test
	public void notImpossibleCheckMateWhenTwoKingsTwoBishopsDifferentSquareColorDifferentPlayers() {
		impossibleCheckMateSetup();
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(1, 0);
		one.putPiece(new Bishop(PieceColor.WHITE));
		two.putPiece(new Bishop(PieceColor.BLACK));
		assertFalse(game.impossibleCheckmate());
	}
	
	@Test
	public void notImpossibleCheckMateWhenTwoKingsTwoBishopsSamePlayer() {
		//obs should never happen anyway, but good to test
		impossibleCheckMateSetup();
		Square one = board.getSquare(0, 0);
		Square two = board.getSquare(2, 0);
		one.putPiece(new Bishop(PieceColor.WHITE));
		two.putPiece(new Bishop(PieceColor.WHITE));
		assertFalse(game.impossibleCheckmate());
	}
	
	@Test
	public void stalemateIfNotCheckMate() {
		board.getSquare(1, 0).putPiece(new King(PieceColor.WHITE));
		board.getSquare(0, 2).putPiece(new Queen(PieceColor.BLACK));
		board.getSquare(2, 2).putPiece(new King(PieceColor.BLACK));
		assertFalse(game.checkmate());
		assertTrue(game.stalemate());
	}

	@Test
	public void threeFoldRepetitionWithEqualBoards() {
		IBoard board = new Board(8, PieceColor.WHITE);
		ArrayList<IBoard> bh = new ArrayList<>();
		bh.add(board);
		bh.add(board);
		bh.add(board);
		bh.add(board);
		bh.add(board);
		bh.add(board);
		game.setBoardHistory(bh);
		assertTrue(game.threefoldRepetition());
	}
	
	@Test
	public void notThreeFoldRepetitionFourTurns() {
		IBoard board = new Board(8, PieceColor.WHITE);
		ArrayList<IBoard> bh = new ArrayList<>();
		bh.add(board);
		bh.add(board);
		bh.add(board);
		bh.add(board);
		game.setBoardHistory(bh);
		assertFalse(game.threefoldRepetition());
	}
	
	@Test
	public void threeFoldRepetitionFiveTurnsDifferentBoardsForDifferentPlayers() {
		IBoard board = new Board(8, PieceColor.WHITE);
		IBoard board1 = new Board(8, PieceColor.WHITE);
		IBoard board2 = new Board(8, PieceColor.WHITE);
		IBoard other = new Board(8, PieceColor.WHITE);
		other.getSquare(0, 0).putPiece(new King(PieceColor.BLACK));
		ArrayList<IBoard> bh = new ArrayList<>();
		bh.add(board);
		bh.add(other);
		bh.add(board1);
		bh.add(other);
		bh.add(board2);
		game.setBoardHistory(bh);
		assertTrue(game.threefoldRepetition());
	}
	
	@Test
	public void notThreeFoldRepetitionNotThreeSamePositionsForSamePlayers() {
		IBoard board = new Board(8, PieceColor.WHITE);
		IBoard other = new Board(8, PieceColor.WHITE);
		other.getSquare(0, 0).putPiece(new King(PieceColor.BLACK));
		ArrayList<IBoard> bh = new ArrayList<>();
		bh.add(board);
		bh.add(board);
		bh.add(other);
		bh.add(other);
		bh.add(board);
		game.setBoardHistory(bh);
		assertFalse(game.threefoldRepetition());
	}
	
	@Test
	public void threeFoldRepetitionAllDifferentButEqualBoards() {
		IBoard board = new Board(8, PieceColor.WHITE);
		IBoard other = new Board(8, PieceColor.WHITE);
		
		other.getSquare(0, 0).putPiece(new King(PieceColor.BLACK));
		IBoard other1 = new Board(8, PieceColor.WHITE);
		other1.getSquare(0, 0).putPiece(new King(PieceColor.BLACK));
		IBoard other2 = new Board(8, PieceColor.WHITE);
		other2.getSquare(0, 0).putPiece(new King(PieceColor.BLACK));
		ArrayList<IBoard> bh = new ArrayList<>();
		bh.add(board);
		bh.add(other1);
		bh.add(other);
		bh.add(other2);
		bh.add(board);
		bh.add(other);
		game.setBoardHistory(bh);
		assertTrue(game.threefoldRepetition());
	}
	

}
