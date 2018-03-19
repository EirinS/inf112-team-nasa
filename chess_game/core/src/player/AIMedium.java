package player;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trædal on 18/03/2018.
 */
public class AIMedium implements AI,Playable {

	PieceColor playerColor;
	PieceColor opponentColor;
	int called = 0;

	/**
	 * 
	 * @param playerColor color of AI
	 */
	public AIMedium(PieceColor playerColor){
		this.playerColor = playerColor;
		if (playerColor==PieceColor.WHITE) {
			this.opponentColor=PieceColor.BLACK;
		}else this.opponentColor=PieceColor.WHITE;
	}
	
	@Override
	public Move calculateMove(IBoard currentBoard) { //the two lists will be of equal length, with the possibleMove.get(i) -> possibleBoard.get(i) 
		Move b = calculateFutureMove(currentBoard);
		return b;
	}
	
	/**
	 * 
	 * @param currentBoard
	 * @param movesAhead
	 * @return returns an array of integers {score,i} where score is the score of the best move and i is its placement in currentBoard.getAvailable moves.
	 */
	
	public Move calculateFutureMove(IBoard currentBoard) {//ArrayList<IBoard> logB, ArrayList<Move> logM) {
		
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		ArrayList<Board> possibleBoards = getPossibleBoards(currentBoard,possibleMoves);
		
		int worstCase = 9999;
		if(playerColor==PieceColor.BLACK) worstCase=-9999;
		ArrayList<int[]> theMoves = new ArrayList<int[]>();
		int[] theMove = {-worstCase, 0};
		
		for (int i=0; i<possibleBoards.size(); i++) {
			List<Move> possibleMovesOpp = possibleBoards.get(i).getAvailableMoves(opponentColor);
			ArrayList<Board> possibleBoardsOpp = getPossibleBoards(possibleBoards.get(i),possibleMovesOpp);
			ArrayList<int[]> findWorst = new ArrayList<int[]>();
			int[] worst = {worstCase,0};
			
			for (int j=0; j<possibleBoardsOpp.size(); j++ ) {
				
				List<Move> possibleMovesEnd = possibleBoardsOpp.get(j).getAvailableMoves(playerColor);
				ArrayList<Board> possibleBoardsEnd = getPossibleBoards(possibleBoardsOpp.get(j),possibleMovesEnd);//i),possibleMovesEnd);
				int[] best = getBestAIScorePlacement(possibleBoardsEnd);
				best[1]=i;
				findWorst.add(best);

			} if (playerColor==PieceColor.WHITE) {
				for (int u=0; u<findWorst.size(); u++) {
					if (findWorst.get(u)[0]<worst[0]) {
						worst=findWorst.get(u);
					}
				}theMoves.add(worst);
			}else {
				for (int u=0; u<findWorst.size(); u++) {
					if (findWorst.get(u)[0]>worst[0]) {
						worst=findWorst.get(u);
					}
				}theMoves.add(worst);
			}
				
		}if (playerColor==PieceColor.WHITE) {
			for (int i=0; i<theMoves.size();i++) {
				if (theMoves.get(i)[0]>theMove[0]) {
					theMove=theMoves.get(i);
				}
				
			}
		}else {
			for (int i=0; i<theMoves.size();i++) {
				if (theMoves.get(i)[0]<theMove[0]) {
					theMove=theMoves.get(i);
				}
			}
		}
		
		return possibleMoves.get(theMove[1]);
	}		
	
	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return calculateMove(board);
	}
	
	public ArrayList<Board> getPossibleBoards(IBoard currentBoard,List<Move> possibleMoves){// this is really messy, new possible boards are created based on all possible outcomes(moves), i did not find a good way to do this, i make a copy shallow copy of unaltered squares and a create new squares where there is changes. i think this will work. the pieces are also only shallow copies. might need to be redone
		ArrayList<Board> possibleBoards = new ArrayList<Board>();
 		for(Move move : possibleMoves) {
			Board possibleBoard = new Board(currentBoard.getDimension(),opponentColor);
			for(Square square : currentBoard.getBoard()) {
				if (move.getFrom()==square) {
				}else if (move.getTo()==square) {
					possibleBoard.getSquare(square.getX(), square.getY()).putPiece(move.getMovingPiece());
				}else if (!square.isEmpty()&&move.getFrom()!=square){
					possibleBoard.getSquare(square.getX(), square.getY()).putPiece(square.getPiece());
				}	
			}
			possibleBoards.add(possibleBoard);
		}
		return possibleBoards;
		
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
			case "K": return 999;
			case "N": return 30;
			case "P": return 10;
			case "Q": return 90;
			case "R": return 50;
			default: throw new IllegalArgumentException("Unknown piece type " + piece.toString());
		}
	}
	
	public int[] getBestAIScorePlacement(ArrayList<Board> possibleBoards) {//
		int[] scoreAndPlace = {0,0};
		//int bestPlacement = 0;
		//int bestScore = 0;
		int i=0;
		if (playerColor==PieceColor.WHITE) {
			scoreAndPlace[0] = -9999;
			for (Board board : possibleBoards) {
				if (getAIScore(board)>=scoreAndPlace[0]) {
					scoreAndPlace[0]=getAIScore(board);
					scoreAndPlace[1]=i;
				}
				i++;
			}
		}else {
			scoreAndPlace[0] = 9999;
			for (Board board : possibleBoards) {
				if (getAIScore(board)<=scoreAndPlace[0]) {
					scoreAndPlace[0]=getAIScore(board);
					scoreAndPlace[1]=i;
				}
				i++;
			}
		}
		//scoreAndPlace[0]=Math.abs(scoreAndPlace[0]);
		return scoreAndPlace;
	}
	
	public int getAIScore(Board possibleBoard) { // for now negative score is black leading, positive is white leading.
		int score = 0;
		ArrayList<Square> squares = possibleBoard.getBoard();
		for (Square square : squares) {
			if(!square.isEmpty()) {
				IPiece piece = square.getPiece();
				int value = getScoreForPieceType(piece);
				if (piece.getColor()==PieceColor.WHITE) {
					score = score + value + getPositionValue(square.getX() ,square.getY());
				}else score = score - value - getPositionValue(square.getX() ,square.getY());
			}
			
		}
		called++;
		return score;
		
	}

}































/*
/**
 * Created by jonas on 16/03/2018.
 *//*
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
*/