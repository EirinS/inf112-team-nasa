package pieces;

public abstract class AbstractPiece implements IPiece {
	/**
	 * Is piece on the board?
	 */
	private boolean inPlay;	
	
	/**
	 * Color of piece
	 */
	private PieceColor color;
	
	@Override
	public boolean isInPlay() {
		return inPlay;		
	}
	
	@Override
	public PieceColor getColor() {
		return color;
	}
	
	
}
