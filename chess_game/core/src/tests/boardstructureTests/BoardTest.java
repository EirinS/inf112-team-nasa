package tests.boardstructureTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import boardstructure.*;
import org.junit.Before;
import org.junit.Test;

import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Rook;

public class BoardTest {
	private int dim = 10;
	private IBoard board = new Board(dim, PieceColor.WHITE);

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void equalBoardsAreEqual() {
		assertEquals(new Board(8, PieceColor.WHITE), new Board(8, PieceColor.WHITE));
	}
	
	@Test
	public void cantCreateIllegalBoard() {
		boolean thrown = false;
		try {
			IBoard board = new Board(-1, PieceColor.WHITE);
		} catch (Exception e){
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void getKingPosCanGetPositionWithKing() {
		Square sq = board.getSquare(3, 3);
		sq.putPiece(new King(PieceColor.WHITE));
		assertEquals(sq, board.getKingPos(PieceColor.WHITE));
	}
	
	@Test
	public void getKingPosCanNotGetPositionWithRook() {
		Square sq = board.getSquare(3, 3);
		sq.putPiece(new Rook(PieceColor.WHITE));
		assertEquals(null, board.getKingPos(PieceColor.WHITE));
	}

	@Test
	public void checkHeightOfBoard() {
		assertEquals(board.getHeight(), dim);
	}
	
	@Test
	public void checkWidthOfBoard() {
		assertEquals(board.getWidth(), dim);
	}
	
	@Test
	public void checkIfBoardIsSquare() {
		assertEquals(board.getWidth(), board.getHeight());
	}
	
	@Test
	public void boardHasNoNullSpots() {
		assertFalse(board.getSquares().contains(null));
	}
	
	@Test
	public void returnsCorrectXCoordinateForSquare() {
		assertEquals(board.getSquare(3, 2).getX(), 3);
	}
	
	@Test
	public void returnsCorrectYCoordinateForSquare() {
		assertEquals(board.getSquare(3, 2).getY(), 2);
	}
	
	@Test
	public void returnsSameSquare() {
		Square sq = board.getSquare(1, 4);
		board.getSquare(1, 4).putPiece(new Rook(PieceColor.WHITE));
		assertEquals(sq, board.getSquare(1, 4));
	}
	
	
	@Test
	public void illegalMoveToLargerThanHeight() {
		assertFalse(board.withinBoard(new Square(dim,dim-1)));
	}
	
	@Test
	public void illegalMoveToLargerThanWidth() {
		assertFalse(board.withinBoard(new Square(dim-1,dim)));
	}
	
	@Test
	public void illegalMoveToNegativeHeight() {
		assertFalse(board.withinBoard(new Square(-1,0)));
	}
	
	@Test
	public void illegalMoveToNegativeWidth() {
		assertFalse(board.withinBoard(new Square(0,-1)));
	}
	
	@Test
	public void legalMoveToFreeSquare() {
		assertTrue(board.withinBoard(new Square(0, 0)));
	}
	
	@Test
	public void illegalMoveToTakenSquare() {
		Square sq = new Square(0,0);
		sq.putPiece(new Rook(PieceColor.WHITE));
		assertFalse(board.movable(sq));
	}
	
	@Test
	public void getAllThreatenedPiecesFindsThreatenedPieces() {
		IBoard newboard = new Board(5, PieceColor.WHITE);
		IPiece whiteRook = new Rook(PieceColor.WHITE);
		IPiece blackRook = new Rook(PieceColor.BLACK);
		IPiece blackKing = new King(PieceColor.BLACK);
		
		//pieces is not here unless moved.
		whiteRook.pieceMoved();
		blackRook.pieceMoved();
		blackKing.pieceMoved();
		
		newboard.getSquare(0, 0).putPiece(blackKing);
		newboard.getSquare(0, 1).putPiece(whiteRook);
		newboard.getSquare(0, 2).putPiece(blackRook);
		
		ArrayList<IPiece> p = newboard.piecesThreatenedByOpponent(PieceColor.BLACK, PieceColor.WHITE);
		assertTrue(p.contains(blackRook));
		assertTrue(p.contains(blackKing));
		assertEquals(2, p.size());
		
		p = newboard.piecesThreatenedByOpponent(PieceColor.WHITE, PieceColor.BLACK);
		assertTrue(p.contains(whiteRook));
		assertEquals(1, p.size());
	}
	
	@Test
	public void copyBoardTestHasSameSquares() {
		IBoard board = new Board(8, PieceColor.WHITE);
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(5, 3).putPiece(r);
		IBoard copy = board.copy();
		
		board.getSquare(2, 2).putPiece(board.getSquare(5, 3).movePiece());
		boolean found = false;
		
		for(Square sq : copy.getSquares()) {
			if(!sq.isEmpty()) {
				found = true;
				assertEquals(sq.getX(), 5);
				assertEquals(sq.getY(), 3);
				assertFalse(sq.getPiece().hasMoved());
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void copyBoardTestHasSameSquaresInSamePlaces() {
		IBoard board = new Board(8, PieceColor.WHITE);
		IPiece r = new Rook(PieceColor.WHITE);
		board.getSquare(5, 3).putPiece(r);
		IBoard copy = board.copy();
		
		assertFalse(board.getSquare(5, 3) == copy.getSquare(5, 3));
		
		assertEquals(board.getSquare(5, 3).getX(), copy.getSquare(5, 3).getX());
		assertEquals(board.getSquare(5, 3).getY(), copy.getSquare(5, 3).getY());
		
		Square to = copy.getSquare(board.getSquare(3, 3).getX(), board.getSquare(3, 3).getY());
		assertEquals(to.getX(), board.getSquare(3, 3).getX());
		assertEquals(to.getY(), board.getSquare(3, 3).getY());
		
		int x = board.getSquare(3, 3).getX();
		int y = board.getSquare(3, 3).getY();
		assertEquals(x, copy.getSquare(x, y).getX());
		assertEquals(y, copy.getSquare(x, y).getY());
	}
	
	private void setUpForMoveTest(BoardListener listener) {
		IPiece rook = new Rook(PieceColor.WHITE);
		board.setListener(listener);
		board.getSquare(5, 5).putPiece(rook);
		board.move(board.getSquare(5, 5), board.getSquare(7, 5));
	}
	
	@Test
	public void moveMovesPiece() {
		setUpForMoveTest(new BoardListener() {

			@Override
			public void promotionRequested(Move move) {}

			@Override
			public void movePerformed(Board board, ArrayList<Move> moves) {
				assertFalse(board.getSquare(7, 5).isEmpty());
				assertTrue(board.getSquare(5, 5).isEmpty());
			}

			@Override
			public void illegalMovePerformed(int fromX, int fromY) { }
		});
	}
	
	@Test
	public void moveSavesMoveInHistory() {
		setUpForMoveTest(new BoardListener() {

			@Override
			public void promotionRequested(Move move) {}

			@Override
			public void movePerformed(Board board, ArrayList<Move> moves) {
				assertEquals(1, board.getHistory().size());
			}

			@Override
			public void illegalMovePerformed(int fromX, int fromY) {

			}
		});
	}
	

}
