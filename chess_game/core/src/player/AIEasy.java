package player;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;

import java.util.ArrayList;
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
	public Move calculateMove(IBoard currentBoard) {
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		
		/////////////////////////////////this is just for testing
		if (possibleMoves.isEmpty()) {
			String outpt = "AIEasy has no moves, game is draw";
			PieceColor opp = PieceColor.BLACK;
			if (playerColor==PieceColor.BLACK) {opp=PieceColor.WHITE;}
			ArrayList<IPiece> allPieces= currentBoard.piecesThreatenedByOpponent(playerColor,opp);
			for (IPiece piece : allPieces) {
				if (piece.toString()=="K") {
					outpt = "AIEasy is checkmate.";
									}
			}System.out.println(outpt);
		}
		///////////////////////////////
		int num = rand.nextInt(possibleMoves.size());

		return possibleMoves.get(num);
	}

	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return calculateMove(board);
	}

}
