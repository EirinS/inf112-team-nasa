package pieces.pieceClasses;

import java.util.ArrayList;
import boardstructure.IBoard;
import boardstructure.Move;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;

/**
 * 
 * @author Sofia
 */
public class Bishop extends AbstractPiece {
	
	public Bishop (PieceColor enumColor){
		color = enumColor;
		inPlay = true;
	}

	@Override
	protected ArrayList<Move> allFreeMoves(int x, int y, IBoard board, PieceColor playerOne) {
		ArrayList<Move> reachable = new ArrayList<Move>();
		reachable.addAll(possibleMoves(board.getSquare(x, y), board, true));
		reachable.addAll(possibleMoves(board.getSquare(x, y), board, false));
		return reachable;
	}
	
	private int determineY(boolean ascending, int yCoordinate){
		int y = -10;
		if (ascending){
			//When y-coordinate is ascending
			y = yCoordinate+1;
		} else {
			//When y-coordinate is descending
			y = yCoordinate-1;
		}
		return y;		
	}
	
	private ArrayList<Move> possibleMoves (Square origin, IBoard board, boolean yAscending){
		ArrayList<Move> canBeReached = new ArrayList<Move>();
		Square square = new Square(0, 0);
		int yCoordinate = determineY(yAscending, origin.getY());
		
		// Depending on the boolean ascending, the following squares are added to the list:
		//TRUE: (Ascending x, ascending Y) || FALSE: (Ascending x, descending Y)
		for(int i = origin.getX()+1; i < board.getDimension(); i++){
			if (yCoordinate >= board.getDimension() || yCoordinate < 0) {
				break;
			}
			square = board.getSquare(i, yCoordinate); 
			if (yAscending) { yCoordinate++;}
			else { yCoordinate--;}
			//Checks if non-empty field is opponent, adds to the list then breaks the loop if true 
			if (!square.isEmpty() && this.color != square.getPiece().getColor()){
				canBeReached.add(getMove(origin, square, board));
				break;
			}
			//Checks if square is inhabited by piece of the same team. Breaks the loop if it is.
			else if (!square.isEmpty() && this.color == square.getPiece().getColor()){
				break;
			//Empty squares are added
			}
			else{
				canBeReached.add(getMove(origin, square, board));
			}
		}
		yCoordinate = determineY(yAscending, origin.getY());
		//Logic identical to loop above. 
		//TRUE: (Descending x, ascending Y) || FALSE: (Decending x, descending Y)
		for (int i = origin.getX()-1; i > 0; i--){
			if (yCoordinate >= board.getDimension() || yCoordinate < 0) {
				break;
			}
			square = board.getSquare(i, yCoordinate);
			
			if (yAscending) { yCoordinate++;}
			else { yCoordinate--;}
			
			
			if (!square.isEmpty() && this.color != square.getPiece().getColor()){
				canBeReached.add(getMove(origin, square, board));
				break;
			}
			else if (!square.isEmpty() && this.color == square.getPiece().getColor()){
				break;
			}
			else{
				canBeReached.add(getMove(origin, square, board));
			}
		}
		return canBeReached;
	}
	
	@Override
	public String toString() {
		return "B";
	}
}
	