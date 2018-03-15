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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capturedPiece == null) ? 0 : capturedPiece.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((moveType == null) ? 0 : moveType.hashCode());
		result = prime * result + ((movingPiece == null) ? 0 : movingPiece.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (capturedPiece == null) {
			if (other.capturedPiece != null)
				return false;
		} else if (!capturedPiece.equals(other.capturedPiece))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (moveType != other.moveType)
			return false;
		if (movingPiece == null) {
			if (other.movingPiece != null)
				return false;
		} else if (!movingPiece.equals(other.movingPiece))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
