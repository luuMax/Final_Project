public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, // self explanatory, only pawns can move
        INVINCIBLE_PAWNS, // pawns cannot be captured
        BACK_IT_UP, // All pawns must move backwards 1 square IF POSSIBLE
        SANCTUARY, // player chooses a square; Any piece that is on that square CANNOT be captured for the duration of the modifier
        EXPLODING_PIECE, // A random knight is chosen (of the side of the player choosing the modifier) and will explode in 10 turns (may change due to balancing)
        SNIPER_BISHOP; // A random bishop is chosen (of the side of the player choosing the modifier). This bishop can move through pieces

        @Override
        public String toString() {
            switch (this) {
                case PAWNS_ONLY:      return "PawnStars";
                case INVINCIBLE_PAWNS: return "[Title Card] Pawns";
                case BACK_IT_UP: return "Back that Ahh up";
                case SANCTUARY: return "Sanctuary";
                /*case KNIGHTS_ONLY:    return "Knights Only";
                case BISHOPS_ONLY:    return "Bishops Only";
                case ROOKS_ONLY:      return "Rooks Only";
                case QUEENS_ONLY:     return "Queens Only";
                case KINGS_ONLY:      return "Kings Only";*/
                case EXPLODING_PIECE: return "Mi Bomboclart";
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
