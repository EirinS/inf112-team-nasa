package game;

import boardstructure.Move;

public interface CheckerboardListener {
    void onPieceClick(int x, int y);
    void onMoveRequested(int fromX, int fromY, int toX, int toY);
}
