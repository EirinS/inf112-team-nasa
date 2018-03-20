package player;

public enum AILevel {
	EASY, INTERMEDIATE, HARD;

	public static AILevel getAILevel(String str) {
		for (AILevel aiLevel : AILevel.values()) {
			if (aiLevel.toString().equals(str))
				return aiLevel;
		}

		// TODO: 20.03.2018 dette burde aldri skje, men grei å ha.
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
				return "Hard (not implemented yet)";
		}

		// TODO: 20.03.2018 dette burde aldri skje, men grei å ha.
		throw new IllegalStateException();
	}
}
