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

	public Move(Square from, Square to, IPiece movingPiece, IPiece capturedPiece) {
		this.from = from;
		this.to = to;
		this.movingPiece = movingPiece;
		this.capturedPiece = capturedPiece;
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

	//Algebraic notation toString
	@Override
	public String toString() {
		String x = "";
		if(capturedPiece != null) {
			x = "x";
		}
		return movingPiece.toString()+from.toString()+x+to.toString();
	}
}
