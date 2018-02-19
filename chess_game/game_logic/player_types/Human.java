package game_logic.player_types;

import game_logic.ChessMove;
import game_logic.Player;
import piece_logic.PieceColor;

/**
 * Created by jonas on 15/02/2018.
 */
public class Human extends Player implements ChessMove {

	public Human(PieceColor pieceColor){
		super(pieceColor);
	}

	@Override
	public void makeMove() {

	}
}
