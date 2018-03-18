package player;

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
		for(Square s : currentBoard.getSquares()) {
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
		if(playerColor == PieceColor.BLACK) {
			return calculate(3, getBoardState(board), board, PieceColor.WHITE);
		}else{
			return calculate(3, getBoardState(board), board, PieceColor.BLACK);
		}
	}

	private int calcFromColor(int depth, int score, IBoard board, PieceColor color) {

		if(depth == 0) return score;

		List<Move> moves = board.getAvailableMoves(color);
		int state = score;

		for(Move currentMove : moves){

			int sum = 0;
			int getPosSumBefore = getPositionValue(currentMove.getFrom().getX(), currentMove.getFrom().getY());
			int getPosSumAfter = getPositionValue(currentMove.getTo().getX(), currentMove.getTo().getY());
			int posChange = getPosSumAfter - getPosSumBefore;
			sum -= posChange;

			IPiece captured = currentMove.getCapturedPiece();
			if(captured != null) {
				sum -= getScoreForPieceType(captured);
				currentMove.getTo().takePiece();
			}
			int possibleState = score;
			if(color == playerColor) {
				sum = sum * -1;
				int possibleState2 = sum+score;
				if(possibleState2 > state) possibleState = possibleState2;
			}else{
				sum = sum + state;
				if(sum < state) {
					state = sum;
				}
			}

			int deeper = possibleState;

			if(color == playerColor) {
				PieceColor colorDeeper;
				if(playerColor == PieceColor.BLACK) {
					colorDeeper = PieceColor.WHITE;
				}else {
					colorDeeper = PieceColor.BLACK;
				}
				deeper = calcFromColor(depth - 1, possibleState, board, colorDeeper);
			}else {
				deeper = calcFromColor(depth - 1, possibleState, board, playerColor);
			}

			if(color == playerColor) {
				if(deeper > state) state = deeper;
			}else {
				if(deeper < state) state = deeper;
			}

			if(captured != null) {
				currentMove.getTo().putPiece(captured);
			}
		}

		return state;
	}

	private Move calculate(int depth, int score, IBoard board, PieceColor color) {

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
			}

			//Take piece out from board so opponent cant use it in board state calculation
			if(captured != null) {
				currentMove.getTo().takePiece();
			}
			int sumForOpponent = calcFromColor(0, sum, board, color);

			int newSum = sumForOpponent;

			if(depth - 1 > 0) {
				newSum = calcFromColor(depth - 1, sumForOpponent, board, playerColor);
			}
			//Undo the move
			if(captured != null) {
				currentMove.getTo().putPiece(captured);
			}

			if(newSum > state) {
				System.out.println("Updated state");
				state = newSum;
				bestMove = currentMove;
			}
		}
		//System.out.println(indent+&quot;max: &quot;+currentMax);
		System.out.println("Best move   : " + bestMove);
		System.out.println("Current sum : " + state);
		return bestMove;
	}


	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return null;
	}

}
