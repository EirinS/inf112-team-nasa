package game.chessGame;

import player.AILevel;

public enum GameType {
	CHESS960, REGULAR, BULLET, BLITZ, RAPID;

	public static GameType getGameType(String selected) {
			for (GameType type: GameType.values()) {
				if (type.toString().equals(selected))
					return type;
			}

			// Dette burde aldri skje, men grei å ha.
			throw new IllegalStateException();
	}
	
	@Override
	public String toString() {
		switch(this) {
		case CHESS960 : return "Chess960";
		case REGULAR : return "Regular";
		case BULLET : return "Bullet";
		case BLITZ : return "Blitz";
		case RAPID : return "Rapid";
		default : return "none";
		}
		
	}
}
