package setups;

import boardstructure.Board;
import boardstructure.BoardListener;
import pieces.PieceColor;

public abstract class AbstractSetup {

    PieceColor getPieceColor(int y, boolean playerWhite) {
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

    public Board getInitialPosition(PieceColor playerColor) {
        return getInitialPosition(playerColor, null);
    }

    abstract Board getInitialPosition(PieceColor playerColor, BoardListener listener);
}