package boardstructure;

import pieces.IPiece;

/**
 * Created by jonas on 12/03/2018.
 */
public class Move {

	//Moves should be immutable
	private Square from;
	private Square to;
	private IPiece movingPiece;
	private IPiece capturedPiece;
	private MoveType moveType;

	public Move(Square from, Square to, IPiece movingPiece, IPiece capturedPiece, MoveType type) {
		this.from = from;
		this.to = to;
		this.movingPiece = movingPiece;
		this.capturedPiece = capturedPiece;
		this.moveType = type;
	}

	public Square getFrom() {
		return from;
	}

	public Square getTo() {
		return to;
	}

	public IPiece getMovingPiece() {
		return movingPiece;
	}

	public IPiece getCapturedPiece() {
		return capturedPiece;
	}
	
	public MoveType getMoveType() {
		return moveType;
	}

	//Algebraic notation toString
	@Override
	public String toString() {
		String x = "";
		if(capturedPiece != null) {
			x = "x";
		}
		return movingPiece.toString()+from.toString()+x+to.toString();
	}
	

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Move))
			return false;
		if(!obj.toString().equals(this.toString())) {
			return false;
		}
		if (!(getMoveType() == ((Move) obj).getMoveType())) {
			return false;
		}
		return true;
	}
}
