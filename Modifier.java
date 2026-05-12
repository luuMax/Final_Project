public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, KNIGHTS_ONLY, BISHOPS_ONLY, ROOKS_ONLY, QUEENS_ONLY, KINGS_ONLY, EXPLODING_PIECE
    }

    private Piece affectedPiece; // used for modifiers that only apply to a single piece
    private int turnsRemaining;
    private Type type;

    // used for modifiers that affect a single piece
    public Modifier(int turns, Type type, Piece affectedPiece) {
        turnsRemaining = turns;
        this.type = type;
        this.affectedPiece = affectedPiece;
    }

    // used for board-wide modifiers
    public Modifier(int turns, Type type) {
        this(turns, type, null);
    }

    public Type getType() {
        return type;
    }
    
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    public void decrementTurns() {
        turnsRemaining--;
    }

    public boolean isExpired() {
        if (turnsRemaining <= 0) {
            return true;
        }
        return false;
    }

    public Piece getAffectedPiece() {
        return affectedPiece;
    }
}
