package game_logic.player_types;

import game_logic.ChessMove;
import game_logic.Player;
import piece_logic.PieceColor;

/**
 * Created by jonas on 15/02/2018.
 */
public class AI extends Player implements ChessMove{

	public AI(PieceColor pieceColor) {
		super(pieceColor);
	}

	@Override
	public void makeMove() {

	}

}
