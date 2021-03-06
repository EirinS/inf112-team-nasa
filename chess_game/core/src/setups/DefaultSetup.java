package setups;

import boardstructure.Board;
import boardstructure.BoardListener;
import game.chessGame.GameType;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.*;

public class DefaultSetup extends AbstractSetup {

    @Override
    public Board getInitialPosition(PieceColor playerColor, BoardListener listener) {
        boolean playerWhite = playerColor == PieceColor.WHITE;
        Board board = new Board(8, playerColor, listener, GameType.REGULAR);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                PieceColor color = getPieceColor(y, playerWhite);
                if (y == 1 || y == 6) {

                    // We're at second row for one of the colors => Pawn.
                    board.getSquare(x, y).putPiece(new Pawn(color));
                } else if (y == 0 || y == 7) {
                    IPiece piece = null;

                    // We're at top/bottom row
                    switch (x) {
                        case 0:
                        case 7:
                            piece = new Rook(color);
                            break;
                        case 1:
                        case 6:
                            piece = new Knight(color);
                            break;
                        case 2:
                        case 5:
                            piece = new Bishop(color);
                            break;
                        case 3:
                            if (playerWhite) piece = new Queen(color);
                            else piece = new King(color);
                            break;
                        case 4:
                            if (playerWhite) piece = new King(color);
                            else piece = new Queen(color);
                            break;
                    }
                    board.getSquare(x, y).putPiece(piece);
                }
            }
        }
        return board;
    }
}
