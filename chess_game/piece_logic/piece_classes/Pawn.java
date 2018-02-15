package piece_logic.piece_classes;

import piece_logic.Piece;

/**
 * Created by jonas on 15/02/2018.
 */
public class Pawn extends Piece {
	boolean isPromoted;
	Piece promotedTo;
	MoveDirection moveDirection;

	public Pawn(MoveDirection moveDirection){
		this.isPromoted = false;
		this.promotedTo = null;
		this.moveDirection = moveDirection;
	}
}
