package setups;

import boardstructure.Board;
import boardstructure.Square;
import pieces.PieceColor;
import pieces.pieceClasses.*;

public class DefaultSetup implements Setup {

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
        Board board = new Board(8);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                PieceColor color = getPieceColor(y, playerWhite);
                Square square = null;
                if (y == 1 || y == 6) {

                    // We're at second row for one of the colors => Pawn.
                    square = new Square(x, y, new Pawn(color));
                } else if (y == 0 || y == 7) {

                    // We're at top/bottom row
                    switch (x) {
                        case 0:
                        case 7:
                            square = new Square(x, y, new Rook(color));
                            break;
                        case 1:
                        case 6:
                            square = new Square(x, y, new Knight(color));
                            break;
                        case 2:
                        case 5:
                            square = new Square(x, y, new Bishop(color));
                            break;
                        case 3:
                            square = new Square(x, y, new Queen(color));
                            break;
                        case 4:
                            square = new Square(x, y, new King(color));
                            break;
                    }
                } else {
                    square = new Square(x, y);
                }
                board.addSquare(square);
            }
        }
        return board;
    }
}
