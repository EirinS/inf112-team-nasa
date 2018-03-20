package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boardstructure.Board;
import boardstructure.IBoard;
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
	public void piecesAreEqualWhenEqualFieldVariablesPiece() {
		assertTrue(game.piecesAreEqual(new Rook(PieceColor.WHITE),(new Rook(PieceColor.WHITE))));
	}

	@Test
	public void piecesAreNotEqualWhenNotEqualFieldVariablesPiece() {
		IPiece rook = new Rook(PieceColor.WHITE);
		rook.pieceMoved();
		assertFalse(game.piecesAreEqual(rook, (new Rook(PieceColor.WHITE))));
	}

	@Test
	public void containsWorksWithDifferentBoardsButEqualFieldVariablesForSquare() {
		board.getSquare(1, 1).putPiece(new Rook(PieceColor.WHITE));
		Square sq = other.getSquare(1, 1);
		sq.putPiece(new Rook(PieceColor.WHITE));
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
				board.move(one, two);
			else 
				board.move(two, one);

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
				board.move(board.getSquare(5, 5), board.getSquare(5, 4));
				board.move(two, one);
			}
			else {
				if(i % 2 == 0)
					board.move(one, two);
				else 
					board.move(two, one);
			}
		}
		//does two moves at 25
		assertEquals(moves+1, board.getHistory().size());
		assertFalse(game.fiftyMoves());
	}
	
	@Test
	public void canFindCheckMate() {
		King k = new King(PieceColor.BLACK);
		board.getSquare(3, 0).putPiece(k);
		board.getSquare(3, 1).putPiece(new Queen(PieceColor.WHITE));
		board.getSquare(4, 2).putPiece(new King(PieceColor.WHITE));
		assertTrue(game.checkmate());
	}
	
	@Test
	public void cantFindCheckMateIfNotCheckMateIfYouCantMoveOnOtherPlayersTurn() {
		King k = new King(PieceColor.WHITE);
		board.getSquare(3, 0).putPiece(k);
		board.getSquare(3, 1).putPiece(new Queen(PieceColor.BLACK));
		board.getSquare(4, 2).putPiece(new King(PieceColor.BLACK));
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
		board.getSquare(1, 0).putPiece(new King(PieceColor.BLACK));
		board.getSquare(0, 2).putPiece(new Queen(PieceColor.WHITE));
		board.getSquare(2, 2).putPiece(new King(PieceColor.WHITE));
		assertFalse(game.checkmate());
		assertTrue(game.stalemate());
	}


}
