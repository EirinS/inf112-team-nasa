package player;

import boardstructure.Board;
import boardstructure.Move;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;

import java.util.List;

/**
 * Created by jonas on 16/03/2018.
 */
public class AIMedium implements AI,Playable {

	PieceColor playerColor;

	int aiScore = 0;
	int opponentScore = 0;

	public AIMedium(PieceColor playerColor){
		this.playerColor = playerColor;
	}

	private void getBoardState(Board currentBoard){
		for(Square s : currentBoard.getBoard()) {

		}
	}

	private int getScoreForPieceType(IPiece piece){
		switch (piece.toString()) {
			case "B": return 30;
			case "K": return 99999;
			case "N": return 30;
			case "P": return 10;
			case "Q": return 90;
			case "R": return 50;
			default: throw new IllegalArgumentException("Unknown piecetype");
		}
	}


	@Override
	public Move calculateMove(Board currentBoard) {
		List<Move> availableMoves = currentBoard.getAvailableMoves(playerColor);

		return null;
	}

	@Override
	public Move makeMove(Board board, Square from, Square to) {
		return null;
	}

}
