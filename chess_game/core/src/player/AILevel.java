package player;

import pieces.PieceColor;

public enum AILevel {
	EASY {

		@Override
		public AI getAI(PieceColor pieceColor) {
			return new AIEasy(pieceColor);
		}
	}, INTERMEDIATE {

		@Override
		public AI getAI(PieceColor pieceColor) {
			return new AIMedium(pieceColor);
		}
	}, HARD {

		@Override
		public AI getAI(PieceColor pieceColor) {
			return new AIHard(pieceColor); 
		}
	};

	public abstract AI getAI(PieceColor pieceColor);

	public static AILevel getAILevel(String str) {
		for (AILevel aiLevel : AILevel.values()) {
			if (aiLevel.toString().equals(str))
				return aiLevel;
		}

		// Dette burde aldri skje, men grei å ha.
		throw new IllegalStateException();
	}

	@Override
	public String toString() {
		switch (this) {
			case EASY:
				return "Easy";
			case INTERMEDIATE:
				return "Intermediate";
			case HARD:
				return "Hard";
		}

		// Dette burde aldri skje, men grei å ha.
		throw new IllegalStateException();
	}
}
