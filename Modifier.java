public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, // self explanatory, only pawns can move
        INVINCIBLE_PAWNS, // pawns cannot be captured
        BACK_IT_UP, // All pawns must move backwards 1 square IF POSSIBLE
        SANCTUARY, // player chooses a square; Any piece that is on that square CANNOT be captured for the duration of the modifier
        EXPLODING_PIECE, // A random knight is chosen (of the side of the player choosing the modifier) and will explode in 10 turns (may change due to balancing)
        SNIPER_BISHOP, // A random bishop is chosen (of the side of the player choosing the modifier). This bishop can move through pieces
        //HORSE_RACE, // both players bet on a "horse race", whoever wins gets a powerup on a random knight on their side
        BRICK, // player creates a brick on a chosen empty square. No pieces may enter the square or move through it
        PORTAL, // Player chooses two squares. At the end of black's turn, the pieces on the portals swap. Replaces any existing portal.
        FILE_SWAP; // Two random files swap
        

        /**
         * Kinds of modifiers that are EASY to add:
         *
         * 1. INVINCIBLE_[PIECE] — a piece type cannot be captured.
         *    Add a check in isBlockedByModifier() similar to INVINCIBLE_PAWNS.
         *
         * 2. RANDOM PIECE TARGET — a random piece of a specific type gets an effect.
         *    Add to offeredModifiers() eligibility check, then handle in handleModifierChoice()
         *    similar to EXPLODING_PIECE and SNIPER_BISHOP.
         *
         * 3. EXPIRE EFFECT — something happens to a square or piece when the modifier runs out.
         *    Add a condition in handleExpiredModifiers() similar to EXPLODING_PIECE.
         *
         * 4. INSTANT BOARD EFFECT — immediately changes piece positions when applied.
         *    Add a condition at the top of addModifier() similar to BACK_IT_UP.
         *
         * 5. PLAYER CHOOSES SQUARE — player clicks a square after selecting the modifier.
         *    Add a placing[ModifierName] flag in BoardUI and handle placement in handleTileClick()
         *    similar to SANCTUARY. Also add to isBlockedByModifier() if the square should be protected.
         */

        @Override
        public String toString() {
            switch (this) {
                case PAWNS_ONLY:      return "PawnStars";
                case INVINCIBLE_PAWNS: return "[Title Card] Pawns";
                case BACK_IT_UP: return "Back that Ahh up";
                case SANCTUARY: return "Mi casa es su casa";
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
            default:           return null;
        }
    }

    public String getDescription(Modifier.Type type) {
        switch (type) {
            case PAWNS_ONLY: return "Only pawns can move for 5 turns";
            case INVINCIBLE_PAWNS: return "Pawns cannot be captured for 5 turns";
            case BACK_IT_UP: return "All pawns must move backwards 1 square if possible";
            case SANCTUARY: return "Choose a square. Whatever piece is on that square is invincible for 5 turns";
            case EXPLODING_PIECE: return "A random knight on your side will explode in 10 turns";
            case SNIPER_BISHOP: return "A random bishop on your side can see through pieces for 5 turns";
            //case HORSE_RACE: return "Bet on a horse race for an advantage";
            case BRICK: return "Choose an empty square. No pieces can move to it or move through it for the rest of the game";
            case PORTAL: return "Player chooses two squares. At the end of black's turn, the pieces on the portals swap. Replaces any existing portal.";
            case FILE_SWAP: return "Two random files swap";
            default: return null;
        }
    }
}
