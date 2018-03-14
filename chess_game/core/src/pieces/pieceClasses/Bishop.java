package pieces.pieceClasses;

import java.util.ArrayList;
import boardstructure.IBoard;
import boardstructure.Square;
import pieces.AbstractPiece;
import pieces.PieceColor;
/**
 * 
 * @author Sofia
 */
public class Bishop extends AbstractPiece{
	
	public Bishop (PieceColor enumColor){
		color = enumColor;
		inPlay = true;
	}

	@Override
	protected ArrayList<Square> allReachableSquares(int x, int y, IBoard board) {
		ArrayList<Square> reachable = new ArrayList<Square>();
		reachable.addAll(reachableSquares(x, y, board, true));
		reachable.addAll(reachableSquares(x, y, board, false));
		return reachable;
	}
	
	private ArrayList<Square> reachableSquares (int x, int y, IBoard board, boolean yAscending){
		ArrayList<Square> canBeReached = new ArrayList<Square>();
		Square square;	
		int yCoordinate;
		
		if (yAscending){
			//When y-coordinate is ascending
			yCoordinate = y+1;
		}
		else {
			//When y-coordinate is descending
			yCoordinate = y-1;
		}
		// Depending on the boolean ascending, the following squares are added to the list:
		//TRUE: (Ascending x, ascending Y) || FALSE: (Ascending x, descending Y)
		for(int i = x+1; i < board.getDimension(); i++){
			
			square = board.getSquare(i, yCoordinate); 
			if (yAscending) { yCoordinate++;}
			else { yCoordinate--;}
			
			//Checks if non-empty field is opponent, adds to the list then breaks the loop if true 
			if (!square.isEmpty() && this.color != square.getPiece().getColor()){
				canBeReached.add(square);
				break;
			}
			//Checks if square is inhabited by piece of the same team. Breaks the loop if it is.
			else if (!square.isEmpty() && this.color == square.getPiece().getColor()){
				break;
			//Empty squares are added
			}
			else{
				canBeReached.add(square); 
			}
		}
		
		//Logic identical to loop above. 
		//TRUE: (Descending x, ascending Y) || FALSE: (Decending x, descending Y)
		for (int i = x-1; i > 0; i--){
			square = board.getSquare(i, yCoordinate);
			if (yAscending) { yCoordinate++;}
			else { yCoordinate--;}
			
			if (!square.isEmpty() && this.color != square.getPiece().getColor()){
				canBeReached.add(square);
				break;
			}
			else if (!square.isEmpty() && this.color == square.getPiece().getColor()){
				break;
			}
			else{
				canBeReached.add(square);
			}
		}
		return canBeReached;
	}
	
	@Override
	public String toString() {
		return "B";
	}
}