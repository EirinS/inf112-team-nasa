package player;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;
import pieces.PieceColor;

import java.util.List;
import java.util.Random;

/**
 * Created by jonas on 12/03/2018.
 */
public class AIEasy implements AI,Playable {

	PieceColor playerColor;
	Random rand = new Random(System.nanoTime());

	public AIEasy(PieceColor playerColor){
		this.playerColor = playerColor;
	}

	@Override
	public Move calculateMove(Board currentBoard) {
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		int num = rand.nextInt(possibleMoves.size());

		return possibleMoves.get(num);
	}

	@Override
	public Move makeMove(Board board, Square from, Square to) {
		return calculateMove(board);
	}

}
