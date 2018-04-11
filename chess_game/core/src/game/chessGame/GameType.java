package game.chessGame;

import player.AILevel;

public enum GameType {
	CHESS960, REGULAR;

	public static GameType getGameType(String selected) {
			for (GameType type: GameType.values()) {
				if (type.toString().equals(selected))
					return type;
			}

			// Dette burde aldri skje, men grei å ha.
			throw new IllegalStateException();
	}
}
