package player;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;

/**
 * Created by jonas on 12/03/2018.
 */
public interface Playable {

	/**
	 * Every player has to be able to perform a move
	 * @return if move was performed or not
	 */
	Move makeMove(Board board, Square from, Square to);
}
