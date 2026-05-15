public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, KNIGHTS_ONLY, BISHOPS_ONLY, ROOKS_ONLY, QUEENS_ONLY, KINGS_ONLY, EXPLODING_PIECE, SNIPER_BISHOP;

        @Override
        public String toString() {
            switch (this) {
                case PAWNS_ONLY:      return "Pawns Only";
                case KNIGHTS_ONLY:    return "Knights Only";
                case BISHOPS_ONLY:    return "Bishops Only";
                case ROOKS_ONLY:      return "Rooks Only";
                case QUEENS_ONLY:     return "Queens Only";
                case KINGS_ONLY:      return "Kings Only";
                case EXPLODING_PIECE: return "Mi Bombo";
                case SNIPER_BISHOP:   return "Sniper Bishop";
                default:              return super.toString();
            }
        }
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

    // returns the type of modifier
    public Type getType() {
        return type;
    }
    
    // returns the number of turns remaining on a modifier
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    // decreases the remaining turns on the modifier
    public void decrementTurns() {
        turnsRemaining--;
    }

    // checks if a modifier is Expired
    public boolean isExpired() {
        if (turnsRemaining <= 0) {
            return true;
        }
        return false;
    }

    // for modifiers that affect a certain piece, such as the exploding knight
    public Piece getAffectedPiece() {
        return affectedPiece;
    }

    // for modifiers that affect a certain class of pieces, such as only Pawns can move
    public Class<?> affectedClass() {
        switch (type) {
            case PAWNS_ONLY:   return Pawn.class;
            case KNIGHTS_ONLY: return Knight.class;
            case BISHOPS_ONLY: return Bishop.class;
            case ROOKS_ONLY:   return Rook.class;
            case QUEENS_ONLY:  return Queen.class;
            case KINGS_ONLY:   return King.class;
            default:           return null;
        }
}
}
