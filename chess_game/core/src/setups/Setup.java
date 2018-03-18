package setups;

import boardstructure.Board;
import sprites.PlayerColor;


public interface Setup {
    Board getInitialPosition(PlayerColor playerColor);
}
