package player;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;

/**
 * Created by jonas on 12/03/2018.
 */
public interface AI {
	public Move calculateMove(Board currentBoard);
}
