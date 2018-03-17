package player;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;

import java.util.List;

/**
 * Created by jonas on 16/03/2018.
 */
public class AIMedium implements AI,Playable {

	static final int AI_MOVE_DEPTH = 2;

	PieceColor playerColor;

	public AIMedium(PieceColor playerColor){
		this.playerColor = playerColor;
	}

	private int getBoardState(IBoard currentBoard){
		int score = 0;
		for(Square s : currentBoard.getBoard()) {
			IPiece p = s.getPiece();

			if (p != null) {
				if(p.getColor() == playerColor) {
					score += getScoreForPieceType(p);
				}else{
					score -= getScoreForPieceType(p);
				}
			}
		}

		return score;
	}

	private int getPositionValue(int row, int column) {
		int[][] positionWeight =
				{
						 {1,1,1,1,1,1,1,1}
						,{2,2,2,2,2,2,2,2}
						,{2,2,3,3,3,3,2,2}
						,{2,2,3,4,4,3,2,2}
						,{2,2,3,4,4,3,2,2}
						,{2,2,3,3,3,3,2,2}
						,{2,2,2,2,2,2,2,2}
						,{1,1,1,1,1,1,1,1}
				};
		return positionWeight[row][column];
	}

	private int getScoreForPieceType(IPiece piece){
		switch (piece.toString()) {
			case "B": return 30;
			case "K": return 99999;
			case "N": return 30;
			case "P": return 10;
			case "Q": return 90;
			case "R": return 50;
			default: throw new IllegalArgumentException("Unknown piece type " + piece.toString());
		}
	}

	@Override
	public Move calculateMove(IBoard board) {
		return calculate(getBoardState(board), board, playerColor);
	}

	private int calcFromOpponent(int score, IBoard board, PieceColor color) {
		List<Move> moves = board.getAvailableMoves(color);
		int state = score;

		for(Move currentMove : moves){

			int sum = score;
			int getPosSumBefore = getPositionValue(currentMove.getFrom().getX(), currentMove.getFrom().getY());
			int getPosSumAfter = getPositionValue(currentMove.getTo().getX(), currentMove.getTo().getY());
			int posChange = getPosSumAfter - getPosSumBefore;
			sum -= posChange;

			IPiece captured = currentMove.getCapturedPiece();
			if(captured != null) {
				sum -= getScoreForPieceType(captured);
				currentMove.getTo().putPiece(null);
			}

			currentMove.getTo().putPiece(captured);

			if(sum < state) {
				state = sum;
			}
		}

		return state;
	}

	private Move calculate(int score, IBoard board, PieceColor color) {

		List<Move> moves = board.getAvailableMoves(playerColor);
		Move bestMove = null;

		int state = -99999;

		for(Move currentMove : moves){

			int sum = score;
			int getPosSumBefore = getPositionValue(currentMove.getFrom().getX(), currentMove.getFrom().getY());
			int getPosSumAfter = getPositionValue(currentMove.getTo().getX(), currentMove.getTo().getY());
			int posChange = getPosSumAfter - getPosSumBefore;
			sum += posChange;

			IPiece captured = currentMove.getCapturedPiece();
			if(captured != null) {
				sum += getScoreForPieceType(captured);
				currentMove.getTo().putPiece(null);
			}

			int sumForOpponent = calcFromOpponent(sum, board, color);

			currentMove.getTo().putPiece(captured);

			if(sumForOpponent > state) {
				state = sum;
				bestMove = currentMove;
			}
		}
		//System.out.println(indent+&quot;max: &quot;+currentMax);
		return bestMove;
	}


	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return null;
	}

}
