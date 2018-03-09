package pieces;

import boardstructure.IBoard;

public abstract class AbstractPiece implements IPiece {
	/**
	 * Is piece on the board?
	 */
	protected boolean inPlay;	
	
	/**
	 * Color of piece
	 */
	protected PieceColor color;

	
	@Override
	public boolean isInPlay() {
		return inPlay;		
	}
	
	@Override
	public PieceColor getColor() {
		return color;
	}
	
	@Override
	public IPiece takePiece() {
		inPlay = false;
		return this;
	}	
}
