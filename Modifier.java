public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, INVINCIBLE_PAWNS, BACK_IT_UP, SANCTUARY,  /*KNIGHTS_ONLY, BISHOPS_ONLY, ROOKS_ONLY, QUEENS_ONLY, KINGS_ONLY,*/ EXPLODING_PIECE, SNIPER_BISHOP;

        @Override
        public String toString() {
            switch (this) {
                case PAWNS_ONLY:      return "Pawns Only";
                case INVINCIBLE_PAWNS: return "Title Card Pawns";
                case BACK_IT_UP: return "Back that Ahh up";
                case SANCTUARY: return "Sanctuary";
                /*case KNIGHTS_ONLY:    return "Knights Only";
                case BISHOPS_ONLY:    return "Bishops Only";
                case ROOKS_ONLY:      return "Rooks Only";
                case QUEENS_ONLY:     return "Queens Only";
                case KINGS_ONLY:      return "Kings Only";*/
                case EXPLODING_PIECE: return "Mi Bombo";
                case SNIPER_BISHOP:   return "Sniper Bishop";
                default:              return super.toString();
            }
        }
    }

    private Piece affectedPiece; // used for modifiers that only apply to a single piece
    private int affectedRow;
    private int affectedCol;
    private int turnsRemaining;
    private Type type;

    // used for modifiers that affect a single piece
    public Modifier(int turnsRemaining, Type type, Piece affectedPiece) {
        this.turnsRemaining = turnsRemaining;
        this.type = type;
        this.affectedPiece = affectedPiece;
        this.affectedRow = -1;
        this.affectedCol = -1;
    }

    // used for board-wide modifiers
    public Modifier(int remainingTurns, Type type) {
        this(remainingTurns, type, null);
        this.affectedRow = -1;
        this.affectedCol = -1;
    }

    // used for modifiers that affect a square
    public Modifier(int turnsRemaining, Type type, int affectedRow, int affectedCol) {
        this.turnsRemaining = turnsRemaining;
        this.type = type;
        this.affectedRow = affectedRow;
        this.affectedPiece = null;
        this.affectedCol = affectedCol;
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

    // for modifiers that affect a certain piece, such as the exploding knight
    public Piece getAffectedPiece() {
        return affectedPiece;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public int getAffectedCol() {
        return affectedCol;
    }

    // for modifiers that affect a certain class of pieces, such as only Pawns can move
    public Class<?> affectedClass() {
        switch (type) {
            case PAWNS_ONLY:   return Pawn.class;
            /*case KNIGHTS_ONLY: return Knight.class;
            case BISHOPS_ONLY: return Bishop.class;
            case ROOKS_ONLY:   return Rook.class;
            case QUEENS_ONLY:  return Queen.class;
            case KINGS_ONLY:   return King.class;*/
            default:           return null;
        }
}
}
