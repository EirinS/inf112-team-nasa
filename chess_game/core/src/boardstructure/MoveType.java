package boardstructure;

public enum MoveType {
    REGULAR, KINGSIDECASTLING, QUEENSIDECASTLING, ENPASSANT, PROMOTION, PAWNJUMP;

    private Object metadata;

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Object getMetadata() {
        return metadata;
    }
}
