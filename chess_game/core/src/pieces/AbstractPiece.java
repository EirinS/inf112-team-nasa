package pieces;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import boardstructure.Square;

public abstract class AbstractPiece {
	/**
	 * Is piece on the board?
	 */
	boolean inPlay;
	
	/**
	 * Graphic of piece
	 */
	public static Image graphic;
	
	
	/**
	 * 
	 * @return true if piece is in play, false if piece has been taken.
	 */
	public boolean isInPlay() {
		return inPlay;		
	}
	
	/**
	 * Find and return all legal positions where a piece can be moved to.
	 * Should check all the rules of the piece and get the legal positions.
	 * @param Square square, the position of piece on board.
	 * @return ArrayList<Square> of legal positions.
	 */
	public abstract ArrayList<Square> legalPositions(Square square);
	
	
}
