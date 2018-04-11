package setups;

import java.util.ArrayList;

import java.util.Random;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Bishop;
import pieces.pieceClasses.King;
import pieces.pieceClasses.Knight;
import pieces.pieceClasses.Pawn;
import pieces.pieceClasses.Queen;
import pieces.pieceClasses.Rook;

/**
 * Starts a game of the type Chess960.
 * It cannot have bishops on same-colored squares
 * and king is placed between rooks. Otherwise the
 * 
 * @author Eirin
 *
 */
public class Chess960Setup implements Setup {

	private Random r = new Random();
	//one set of pieces
	private int queen = 1, king = 1, rook = 2, bishop = 2, knight = 2;
	boolean hasPlacedFirstRook, hasPlacedKing;

	private PieceColor getPieceColor(int y, boolean playerWhite) {
		boolean isBottom = y == 6 || y == 7;
		if (isBottom) {
			if (playerWhite) {
				return PieceColor.WHITE;
			} else {
				return PieceColor.BLACK;
			}
		} else {
			if (playerWhite) {
				return PieceColor.BLACK;
			} else {
				return PieceColor.WHITE;
			}
		}
	}

	@Override
	public Board getInitialPosition(PieceColor playerColor) {
		boolean playerWhite = playerColor == PieceColor.WHITE;
		Board board = new Board(8, playerColor);
		PieceColor color1 = getPieceColor(1, playerWhite);
		PieceColor color2 = getPieceColor(6, playerWhite);
		for(int x = 0; x < 8; x++) {
			board.getSquare(x, 1).putPiece(new Pawn(color1));
			board.getSquare(x, 6).putPiece(new Pawn(color2));
		}

		for (int x = 0; x < 8; x++) {
			boolean putPiece = false;
			while (!putPiece) {
				switch(r.nextInt(5)) {
				case 0: 
					if(hasPlacedFirstRook && king == 1) {
						king--;
						board.getSquare(x, 0).putPiece(new King(color1));
						board.getSquare(x, 7).putPiece(new King(color2));
						putPiece = true;
						hasPlacedKing = true;
					}
					break;
				case 1:
					if (queen == 1) {
						queen--;
						board.getSquare(x, 0).putPiece(new Queen(color1));
						board.getSquare(x, 7).putPiece(new Queen(color2));
						putPiece = true;
					}
					break;
				case 2: 
					if (bishop > 0) {
						bishop--;
						board.getSquare(x, 0).putPiece(new Bishop(color1));
						board.getSquare(x, 7).putPiece(new Bishop(color2));
						putPiece = true;
					}
					break;
				case 3:
					if (knight > 0) {
						knight--;
						board.getSquare(x, 0).putPiece(new Knight(color1));
						board.getSquare(x, 7).putPiece(new Knight(color2));
						putPiece = true;
					}
					break;
				case 4:
					if (rook > 0 && (hasPlacedKing == hasPlacedFirstRook)) {
						rook--;
						hasPlacedFirstRook = true;
						board.getSquare(x, 0).putPiece(new Rook(color1));
						board.getSquare(x, 7).putPiece(new Rook(color2));
						putPiece = true;
					}
					break;
				}

			}
		}
		//board = makeBishopDifferentColoredSquares(board);

		return board;
	}
	
	private Board makeBishopDifferentColoredSquares(Board board) {
		ArrayList<Integer> xss = board.getBishopPos(0);
		int x = xss.get(0), x1 = xss.get(1);
		IPiece swap1 = null, swap2 = null;
		if(board.getSquare(x, 0).squareIsWhite() == board.getSquare(x1, 0).squareIsWhite()) {
			if(board.withinBoard(x-1, 0)) {
				swap1 = board.getSquare(x-1, 0).takePiece();
				swap2 = board.getSquare(x-1, 7).takePiece();
				board.getSquare(x-1, 7).putPiece(board.getSquare(x, 7).takePiece());
				board.getSquare(x-1, 0).putPiece(board.getSquare(x, 0).takePiece());
			} else {
				swap1 = board.getSquare(x+1, 0).takePiece();
				swap2 = board.getSquare(x+1, 7).takePiece();
				board.getSquare(x+1, 7).putPiece(board.getSquare(x, 7).takePiece());
				board.getSquare(x+1, 0).putPiece(board.getSquare(x, 0).takePiece());
			}
		}
		return board;
	}


}
