package pieces;

/**
 * Enum to decide whether a piece is black or white.
 * @author Eirin
 *
 */
public enum PieceColor {

	WHITE {
		@Override
		public PieceColor getOpposite() {
			return BLACK;
		}
	}, BLACK {
		@Override
		public PieceColor getOpposite() {
			return WHITE;
		}
	};

	public abstract PieceColor getOpposite();
}
