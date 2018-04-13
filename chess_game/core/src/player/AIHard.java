package player;

import boardstructure.Board;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.MoveType;
import boardstructure.Square;
import pieces.IPiece;
import pieces.PieceColor;
import pieces.pieceClasses.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trædal on 05/04/2018.
 */
public class AIHard implements AI, Playable {

	private static int rating = 2000;

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
	public AIHard(PieceColor playerColor){
		this.playerColor = playerColor;
		if(playerColor==PieceColor.BLACK) {this.bestCase=-9999; this.loss=99999;}else {this.bestCase=9999; this.loss=-99999;}
		opponentColor = playerColor.getOpposite();
	}
	
	@Override
	public Move calculateMove(IBoard currentBoard) { //the two lists will be of equal length, with the possibleMove.get(i) -> possibleBoard.get(i) 
		this.currentBoard=currentBoard;
		List<Move> possibleMoves = currentBoard.getAvailableMoves(playerColor);
		ArrayList<int[]> theMoves = findBestMovesLooking3Forward(currentBoard, playerColor);
		
		int[] theMove = processThreeBest(theMoves,playerColor);// findTheMove(theMoves, playerColor);//keepCalculating(theMoves);
		return possibleMoves.get(theMove[1]);
	}	
	
	private ArrayList<int[]> findBestMovesLooking3Forward(IBoard board, PieceColor playerTurn) {
		List<Move> possibleMoves = board.getAvailableMoves(playerTurn);
		ArrayList<Board> possibleBoards = getPossibleBoards(board,possibleMoves, playerTurn);
		ArrayList<int[]> theMoves = new ArrayList<int[]>();//here all the moves and their valued score will be placed
		
		for (int i=0; i<possibleBoards.size(); i++) {//for every move possible to make for the original board
			List<Move> possibleMovesOpp = possibleBoards.get(i).getAvailableMoves(playerTurn.getOpposite());
			if (possibleMovesOpp.isEmpty()) {//opponent is checkmate or there is a draw
				if (isCheckmate(possibleBoards.get(i),playerTurn.getOpposite())||considerDraw(board)) {/////////////////////////////////////////
					theMoves.add(new int[] {-loss,i});
					return theMoves;
				}else {
					int[] forcedDraw = {-bestCase,i};//AI is forced by the opponent to make a draw, this is just one of the valid moves for the AI (may be the best/only one) 
					theMoves.add(forcedDraw);
				}
			}else {
				ArrayList<Board> possibleBoardsOpp = getPossibleBoards(possibleBoards.get(i),possibleMovesOpp,playerTurn.getOpposite());
				ArrayList<int[]> findWorst = new ArrayList<int[]>();
				
				for (int j=0; j<possibleBoardsOpp.size(); j++ ) {//for every move possible after the first move
					findWorst=opponentChoice(possibleBoardsOpp.get(j),findWorst,i,j,playerTurn);
				}
				theMoves=findTheMoves(theMoves, findWorst,playerTurn);
			}	
		}
		
		return theMoves;
	}
	
	@Override
	public Move makeMove(IBoard board, Square from, Square to) {
		return calculateMove(board);
	}
	
	public ArrayList<Board> getPossibleBoards(IBoard board,List<Move> possibleMoves, PieceColor playerTurn){// this is really messy, new possible boards are created based on all possible outcomes(moves), i did not find a good way to do this, i make a copy shallow copy of unaltered squares and a create new squares where there is changes. i think this will work. the pieces are also only shallow copies. 
		ArrayList<Board> possibleBoards = new ArrayList<Board>();
 		for(Move move : possibleMoves) {
			possibleBoards.add(getPossibleBoard(board,move, playerTurn));
		}
		return possibleBoards;
	}
	
	public Board getPossibleBoard(IBoard board,Move move, PieceColor playerTurn){
		Board possibleBoard = new Board(currentBoard.getDimension(),currentBoard.getPlayerOne());
		for(Square square : board.getSquares()) {
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
		return possibleBoard;
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
	
	public int[] getBestAIScorePlacement(ArrayList<Board> possibleBoards,PieceColor playerTurn) {//returns the best score and its placement in passed ArrayList
		int[] scoreAndPlace = {0,0};
		int j=0;
		if (playerTurn==PieceColor.WHITE) {
			scoreAndPlace[0] = -9999;
			for (Board board : possibleBoards) {
				if (getAIScore(board)>=scoreAndPlace[0]) {
					scoreAndPlace[0]=getAIScore(board);
					scoreAndPlace[1]=j;
				}
				j++;
			}
		}else {
			scoreAndPlace[0] = 9999;
			for (Board board : possibleBoards) {
				if (getAIScore(board)<=scoreAndPlace[0]) {
					scoreAndPlace[0]=getAIScore(board);
					scoreAndPlace[1]=j;
				}
				j++;
			}
		}
		return scoreAndPlace;
	}
	
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
	
	private boolean isPromotionMove(Move move) {
		if(move.getMoveType()==MoveType.PROMOTION) {
			return true;
		}
		return false;
	}
	
	
	private boolean considerDraw (IBoard board) {//returns true if draw is positive for AI
			int score = getAIScore(board);
			if  ((score<0&&playerColor==PieceColor.WHITE)||(score>0&&playerColor==PieceColor.BLACK)) {//if AI is under in score and can get a draw, it will do it.
				return true;
			}else {//if it is ahead in score, draw is bad
				return false;
			}
	}

	
	private boolean isCheckmate (Board board, PieceColor playerInCheck) {//returns true if player is checkmate
		ArrayList<IPiece> allPieces = board.piecesThreatenedByOpponent(playerInCheck, playerInCheck.getOpposite());
		for (IPiece piece : allPieces) {
			if (piece.toString()=="K") {
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<int[]> findTheMoves (ArrayList<int[]> theMoves, ArrayList<int[]> findWorst,PieceColor playerTurn) {//find all the best moves after opponent choice
		int[] worst;// = {bestCase,0,0};////////////this is bug, hello bug
		if (playerTurn==PieceColor.WHITE) {
			worst = new int[]{9999,0,0};
			for (int u=0; u<findWorst.size(); u++) {
				if (findWorst.get(u)[0]<worst[0]) {
					worst=findWorst.get(u);
				}
			}theMoves.add(worst);
		}else {
			worst = new int[]{-9999,0,0};
			for (int u=0; u<findWorst.size(); u++) {
				if (findWorst.get(u)[0]>worst[0]) {
					worst=findWorst.get(u);
				}
			}theMoves.add(worst);
		}
		return theMoves;
	}
	
	private int[] findTheMove (ArrayList<int[]> theMoves,PieceColor playerTurn) {// find the best move, after all is said and done and we have a list rating all the different moves
		int[] theMove;//= {-bestCase, 0};
		if (playerTurn==PieceColor.WHITE) {
			theMove= new int[] {-9999, 0};
			for (int i=0; i<theMoves.size();i++) {
				if (theMoves.get(i)[0]>theMove[0]) {
					theMove=theMoves.get(i);
				}
			}
		}else {
			theMove= new int[] {9999, 0};
			for (int i=0; i<theMoves.size();i++) {
				if (theMoves.get(i)[0]<theMove[0]) {
					theMove=theMoves.get(i);
				}
			}
		}
		return theMove;
	}
	
	private int[] processThreeBest(ArrayList<int[]> theMoves,PieceColor playerTurn) {
		
		int[] nr1 = null;
		int[] nr2 = null;
		int[] nr3 = null;
		
		if (playerTurn == PieceColor.WHITE) {
			for (int[] a : theMoves) {
				if (nr1==null) {nr1 = a;}else if(nr1[0]<a[0]){nr3 = nr2;nr2 = nr1;nr1 = a;
				}else if (nr2==null) {nr2 = a;}else if(nr2[0]<a[0]) {nr3 = nr2;nr2 = a;
				}else if (nr3==null||nr3[0]<a[0]) {nr3 = a;}	
			}
		}else {
			for (int[] a : theMoves) {
				if (nr1==null) {nr1 = a;}else if(nr1[0]>a[0]){nr3 = nr2;nr2 = nr1;nr1 = a;
				}else if (nr2==null) {nr2 = a;}else if(nr2[0]>a[0]) {nr3 = nr2;nr2 = a;
				}else if (nr3==null||nr3[0]>a[0]) {nr3 = a;}	
			}
		}
	
		if (nr1[0]==-loss||nr1[0]==bestCase) {
				return nr1;
		}
			
		ArrayList <int[]> threeBestMoves = new ArrayList <int[]>();
		if (nr1.length==4) {
			threeBestMoves.add(nr1);
			if (nr2!=null&&nr2[0]!=loss&&nr2[0]!=-bestCase) {
				threeBestMoves.add(nr2);
				if (nr3!=null&&nr3[0]!=loss&&nr3[0]!=-bestCase) {
					threeBestMoves.add(nr3);
				}
			}
		}else {
			return nr1;
		}
		
		for (int i = 0; i<threeBestMoves.size();i++) {
			int [] a = threeBestMoves.get(i);
			Move move = currentBoard.getAvailableMoves(playerColor).get(a[1]);
			Board board=getPossibleBoard(currentBoard,move, playerColor);
			/*List<Move> bb = board.getAvailableMoves(opponentColor);
			try {
				move = board.getAvailableMoves(opponentColor).get(a[2]);
				board=getPossibleBoard(board,move, opponentColor);
			}catch (IndexOutOfBoundsException e) {
			    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
			}
			
			move = board.getAvailableMoves(playerColor).get(a[3]);
			board=getPossibleBoard(board,move, playerColor);*/
			//board.printOutBoard();
			int[] newShit = findTheMove(findBestMovesLooking3Forward(board,opponentColor),playerTurn.getOpposite());/// need better name, and other stuff
			newShit[1] = threeBestMoves.get(i)[1];
			threeBestMoves.set(i, newShit);

		}
		return findTheMove(threeBestMoves,playerTurn);
	}
	
	
	
	private ArrayList<int[]> opponentChoice (Board board, ArrayList<int[]> findWorst, int i, int j, PieceColor playerTurn) {//takes away all but one move for each board in possibleBoardsOpp(the one move with best score for the opponent remains)
		List<Move> possibleMovesEnd = board.getAvailableMoves(playerTurn);
		if(possibleMovesEnd.isEmpty()) {//AI is checkmate or there is a draw
			if (isCheckmate(board,playerTurn)) {
				int[] lostCase = {loss, i};
				findWorst.add(lostCase);
			}else {
				if (considerDraw (board)) {
					int[] idealDraw = {bestCase,i};
					findWorst.add(idealDraw);
				}else {//if it is ahead in score, draw is bad
					int[] forcedDraw = {-bestCase,i};
					findWorst.add(forcedDraw);
				}
			}
		}else {
			ArrayList<Board> possibleBoardsEnd = getPossibleBoards(board, possibleMovesEnd, playerTurn);
			int[] temp = getBestAIScorePlacement(possibleBoardsEnd,playerColor);
			int[] best = {temp[0],i,j,temp[1]};
			
			findWorst.add(best);
		}
		return findWorst;
	}
	
}


















/*
	private int[] keepCalculating (ArrayList<int[]> theMoves) {
		Move move = null;
		//ArrayList<int[]> theNewMoves=new ArrayList<int[]>();
		int[] value = null;
		for (int[] a : theMoves) {
			if (a[0]==-loss) {
				return a;
			}else if (a[0]==bestCase){
				value = a;
			}
		}
		if (value==null) {
			for (int i=0;i<theMoves.size() ; i++) {
				int[] a = theMoves.get(i);
				if (a.length==4) {
					move = currentBoard.getAvailableMoves(playerColor).get(a[1]);
					Board board=getPossibleBoard(currentBoard,move, playerColor);
					move = board.getAvailableMoves(opponentColor).get(a[2]);
					board=getPossibleBoard(board,move, opponentColor);
					//List<Move> bb = board.getAvailableMoves(playerColor);
					//try {
					move = board.getAvailableMoves(playerColor).get(a[3]);
					board=getPossibleBoard(board,move, playerColor);
					//}catch (IndexOutOfBoundsException e) {
					    //System.err.println("IndexOutOfBoundsException: " + e.getMessage());
					//}
					//bb=null;
					//board.printOutBoard();
					List<Move> possibleMovesOpp = board.getAvailableMoves(opponentColor);
					
					if (possibleMovesOpp.isEmpty()) {//opponent is checkmate or there is a draw
						if (isCheckmate(board,opponentColor)||considerDraw()) {/////////////////////////////////////////
							return a;
						}else {
							a[0] = -bestCase;
							theMoves.set(i, a);
							//int[] forcedDraw = {-bestCase,i};//AI is forced by the opponent to make a draw, this is just one of the valid moves for the AI (may be the best/only one) 
							//theMoves.add(forcedDraw);
						}
					}else {
						ArrayList<Board> possibleBoardsOpp = getPossibleBoards(board,possibleMovesOpp,opponentColor);
						ArrayList<int[]> findWorst = new ArrayList<int[]>();
						
						for (int j=0; j<possibleBoardsOpp.size(); j++ ) {//for every move possible after the first move
							findWorst=opponentChoice(possibleBoardsOpp.get(j),findWorst,a[1],j);
						}
						theMoves.set(i, findTheMove(findWorst));
						//theMoves=findTheMoves(theMoves, findWorst);
					}
				}else {
					///////if move is end of game ???
				}
			}
		}else return value;
		return findTheMove(theMoves);
	}*/