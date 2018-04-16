package boardstructure;

public enum PromotionPiece {
    QUEEN,
    KNIGHT,
    ROOK,
    BISHOP;


    @Override
    public String toString() {
        switch (this) {
            case QUEEN:
                return "q";
            case KNIGHT:
                return "n";
            case ROOK:
                return "r";
            case BISHOP:
                return "b";
        }
        return super.toString();
    }
}