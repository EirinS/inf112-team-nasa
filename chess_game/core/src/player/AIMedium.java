package player;

import boardstructure.*;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trædal on 18/03/2018.
 */
public class AIMedium implements AI, Playable {

	private static int rating = 1500;

	private PieceColor playerColor;
	private PieceColor opponentColor;
	private int called = 0;//just for checking manually, not needed at all

	private int bestCase;
	private int loss;
	private IBoard currentBoard;

	
	/**
	 * 
	 * @param playerColor color of AI
	 */
	public AIMedium(PieceColor playerColor){
		this.playerColor = playerColor;
		if(playerColor==PieceColor.BLACK) {this.bestCase=-9999; this.loss=99999;}else {this.bestCase=9999; this.loss=-99999;}
		opponentColor = playerColor.getOpposite();
	}

	/*public static AIMedium getInstance(PieceColor playerColor) {
		if (instance == null)
			instance = new AIMedium(playerColor);
		instance.playerColor = playerColor;
		return instance;
	}*/
	
	@Override
	public Move calculateMove(IBoard currentBoard) { //the two lists will be of equal length, with the possibleMove.get(i) -> possibleBoard.get(i) 
		this.currentBoard=currentBoard;
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		ArrayList<Board> possibleBoards = getPossibleBoards(currentBoard,possibleMoves, playerColor);
		ArrayList<int[]> theMoves = new ArrayList<int[]>();//here all the moves and their valued score will be placed
		int[] theMove; // if there is no checkmate or draw, this is the move that will be returned
		
		for (int i=0; i<possibleBoards.size(); i++) {//for every move possible to make for the original board
			List<Move> possibleMovesOpp = possibleBoards.get(i).getAvailableMoves(opponentColor);
			if (possibleMovesOpp.isEmpty()) {//opponent is checkmate or there is a draw
				if (isCheckmate(possibleBoards.get(i),opponentColor)||considerDraw()) {/////////////////////////////////////////
					return possibleMoves.get(i);
				}else {
					int[] forcedDraw = {-bestCase,i};//AI is forced by the opponent to make a draw, this is just one of the valid moves for the AI (may be the best/only one) 
					theMoves.add(forcedDraw);
				}
			}else {
				ArrayList<Board> possibleBoardsOpp = getPossibleBoards(possibleBoards.get(i),possibleMovesOpp,opponentColor);
				ArrayList<int[]> findWorst = new ArrayList<int[]>();
				
				for (int j=0; j<possibleBoardsOpp.size(); j++ ) {//for every move possible after the first move
					findWorst=opponentChoice(possibleBoardsOpp.get(j),findWorst,i);
				}
				theMoves=findTheMoves(theMoves, findWorst);
			}	
		}
		theMove = findTheMove(theMoves);
		return possibleMoves.get(theMove[1]);
	}		
	
	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return calculateMove(board);
	}
	
	/**
	 * 
	 * @param currentBoard board that we have
	 * @param possibleMoves all moves possible form currentBoard
	 * @param playerTurn who would be making these moves
	 * @return ArrayList<Board> all possible Boards after all possible Moves are made.
	 */
	public ArrayList<Board> getPossibleBoards(IBoard currentBoard,List<Move> possibleMoves, PieceColor playerTurn){// this is really messy, new possible boards are created based on all possible outcomes(moves), i did not find a good way to do this, i make a copy shallow copy of unaltered squares and a create new squares where there is changes. i think this will work. the pieces are also only shallow copies. 
		ArrayList<Board> possibleBoards = new ArrayList<Board>();
 		for(Move move : possibleMoves) {
			Board possibleBoard = new Board(currentBoard.getDimension(),currentBoard.getPlayerOne());
			for(Square square : currentBoard.getSquares()) {
				if (move.getFrom()==square) {
				}else if (move.getTo()==square) {																																								
					String piece = move.getFrom().getPiece().toString();
					if (piece=="R"||piece=="K"||piece=="P") {
						IPiece copy;
						if (isPromotionMove(move)) {
							copy = new Queen(playerTurn);
						}else  {
							copy = move.getFrom().getPiece().copy();
							copy.pieceMoved();
						}
						possibleBoard.getSquare(square.getX(), square.getY()).putPiece(copy);
					}else {
						possibleBoard.getSquare(square.getX(), square.getY()).putPiece(move.getMovingPiece());
					}
				}else if (!square.isEmpty()&&move.getFrom()!=square){
					possibleBoard.getSquare(square.getX(), square.getY()).putPiece(square.getPiece());
				}	
			}
			possibleBoards.add(possibleBoard);
		}
		return possibleBoards;
	}
	
	/**
	 * 
	 * @param row x position of evaluation
	 * @param column y position of evaluation
	 * @return int a evaluation of piece position
	 */
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
	
	/**
	 * 
	 * @param piece you want the value of
	 * @return int value of piece
	 */
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
	
	/**
	 * 
	 * @param possibleBoards boards to be evaluated to find the best one
	 * @return int[] {score, place} the score of the best board, and its place in possibleBoards 
	 */
	public int[] getBestAIScorePlacement(ArrayList<Board> possibleBoards) {//returns the best score and its placement in passed ArrayList
		int[] scoreAndPlace = {0,0};
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
		return scoreAndPlace;
	}
	
	/**
	 * 
	 * @param possibleBoard board you want evaluated for score
	 * @return the score of the board
	 */
	public int getAIScore(IBoard possibleBoard) { // for now negative score is black leading, positive is white leading.
		int score = 0;
		ArrayList<Square> squares = possibleBoard.getSquares();
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

	@Override
	public PieceColor getPieceColor() {
		return playerColor;
	}		

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public PromotionPiece calculatePromotionPiece(IBoard currentBoard, Move promotionMove) {

		// TODO: Implement logic here.
		return PromotionPiece.QUEEN;
	}


	private boolean isPromotionMove(Move move) {
		if(move.getMoveType()==MoveType.PROMOTION) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return true if AI should try to achieve draw
	 */
	private boolean considerDraw () {//returns true if draw is positive for AI
			int score = getAIScore(currentBoard);
			if  ((score<0&&playerColor==PieceColor.WHITE)||(score>0&&playerColor==PieceColor.BLACK)) {//if AI is under in score and can get a draw, it will do it.
				return true;
			}else {//if it is ahead in score, draw is bad
				return false;
			}
	}

	/**
	 * 
	 * @param board to check
	 * @param playerInCheck player you want to check
	 * @return true if player is checkmate, false otherwise
	 */
	private boolean isCheckmate (Board board, PieceColor playerInCheck) {//returns true if player is checkmate
		PieceColor otherPlayer=PieceColor.BLACK;
		if (playerInCheck==PieceColor.BLACK) {
			otherPlayer=PieceColor.WHITE;
		}
		ArrayList<IPiece> allPieces = board.piecesThreatenedByOpponent(playerInCheck, otherPlayer);
		for (IPiece piece : allPieces) {
			if (piece.toString()=="K") {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param theMoves the moves you have so far that is to be considered
	 * @param findWorst the moves that have been filtered for enemy counterplay
	 * @return ArrayList<int[]> theMoves one score per move, this is the score the AI predicts it has after 3 moves if it does this move.
	 */
	private ArrayList<int[]> findTheMoves (ArrayList<int[]> theMoves, ArrayList<int[]> findWorst) {//find all the best moves after opponent choice
		int[] worst = {bestCase,0};
		if (playerColor==PieceColor.WHITE) {
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
		return theMoves;
	}
	
	/**
	 * 
	 * @param theMoves list of all moves and the AI predictions of their score
	 * @return int[] {score, place} the placement if the move with the best score
	 */
	private int[] findTheMove (ArrayList<int[]> theMoves) {// find the best move, after all is said and done and we have a list rating all the different moves
		int[] theMove = {-bestCase, 0};
		if (playerColor==PieceColor.WHITE) {
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
		return theMove;
	}
	
	/**
	 * 
	 * @param board the board to be considered
	 * @param findWorst the moves that have been filtered for enemy counterplay
	 * @param i placement of the first move in the first List<Move> 
	 * @return ArrayList<int[]> findWorst, list of the score and placement after enemy counterplay
	 */
	private ArrayList<int[]> opponentChoice (Board board, ArrayList<int[]> findWorst, int i) {//takes away all but one move for each board in possibleBoardsOpp(the one move with best score for the opponent remains)
		List<Move> possibleMovesEnd = board.getAvailableMoves(playerColor);
		if(possibleMovesEnd.isEmpty()) {//AI is checkmate or there is a draw
			if (isCheckmate(board,playerColor)) {
				int[] lostCase = {loss, i};
				findWorst.add(lostCase);
			}else {
				if (considerDraw ()) {
					int[] idealDraw = {bestCase,i};
					findWorst.add(idealDraw);
				}else {//if it is ahead in score, draw is bad
					int[] forcedDraw = {-bestCase,i};
					findWorst.add(forcedDraw);
				}
			}
		}else {
			ArrayList<Board> possibleBoardsEnd = getPossibleBoards(board,possibleMovesEnd, playerColor);
			int[] best = getBestAIScorePlacement(possibleBoardsEnd);
			best[1]=i;
			findWorst.add(best);
		}
		return findWorst;
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