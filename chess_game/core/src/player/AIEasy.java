package player;

import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.PromotionPiece;
import boardstructure.Square;
import pieces.PieceColor;

import java.util.List;
import java.util.Random;

/**
 * Created by jonas on 12/03/2018.
 */
public class AIEasy implements AI, Playable {

	private static int rating = 500;

	private PieceColor playerColor;
	Random rand = new Random(System.nanoTime());

	public AIEasy(PieceColor playerColor){
		this.playerColor = playerColor;
	}
	/*
	public static AIEasy getInstance(PieceColor playerColor) {
		if (instance == null)
			instance = new AIEasy(playerColor);
		instance.playerColor = playerColor;
		return instance;
	}*/

	@Override
	public Move calculateMove(IBoard currentBoard) {
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		int num = rand.nextInt(possibleMoves.size());

		return possibleMoves.get(num);
	}

	@Override
	public PieceColor getPieceColor() {
		return playerColor;
	}

	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return calculateMove(board);
	}

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public PromotionPiece calculatePromotionPiece(IBoard currentBoard, Move promotionMove) {

		// TODO: 14/04/2018 Randomized aswell, keep it or stick to queen? :-)
		int i = rand.nextInt(4);
		switch (i) {
			case 0:
				return PromotionPiece.QUEEN;
			case 1:
				return PromotionPiece.KNIGHT;
			case 2:
				return PromotionPiece.ROOK;
			case 3:
				return PromotionPiece.BISHOP;
		}

		// Should never happen
		return null;
	}
}