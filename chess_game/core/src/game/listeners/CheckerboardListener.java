package game.listeners;

public interface CheckerboardListener {
    void onDragPieceStarted(int x, int y);
    void onMoveRequested(int fromX, int fromY, int toX, int toY);
}