package player;

import boardstructure.IBoard;
import boardstructure.Move;
import game.Checkerboard;
import scenes.GameScene;

/**
 * Created by jonas on 11/04/2018.
 */
public class AIThreadHint implements Runnable {

	IBoard board;
	AI ai;
	Checkerboard gs;

	public AIThreadHint(IBoard board, AI ai, Checkerboard gs){
		this.board = board;
		this.ai = ai;
		this.gs = gs;
	}

	@Override
	public void run() {
		Move move = ai.calculateMove(board);
		gs.showHint(move);
	}
}
