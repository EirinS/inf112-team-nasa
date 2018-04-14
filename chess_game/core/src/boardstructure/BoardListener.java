package boardstructure;

import java.util.ArrayList;

public interface BoardListener {

    /**
     * Promotion move has been performed, ask player/AI what piece to promote to.
     * @param move Promotion move
     */
    void promotionRequested(Move move);

    /**
     * Get's called when moves has been performed on the board.
     * @param board Board the move has been performed on.
     * @param moves List of performed moves.
     */
    void movePerformed(Board board, ArrayList<Move> moves);

    /**
     * Get's called when the attempted move is illegal.
     */
    void illegalMovePerformed(int fromX, int fromY);
}
