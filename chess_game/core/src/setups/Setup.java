package setups;

import boardstructure.Board;
import pieces.PieceColor;

public interface Setup {
    Board getInitialPosition(PieceColor playerColor);
}