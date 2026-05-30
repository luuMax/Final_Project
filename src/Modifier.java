package src;

/**
 * A custom data structure that represents any relevant data about a modifier, like name, type of modifier, pieces affected, etc.
 */
public class Modifier {
    /**
     * Defines the type of modifier a modifier can be
     */
    public enum Type {
        /**
         * only pawns can move
         */
        PAWNS_ONLY,
        /**
         * pawns cannot be captured
         */
        INVINCIBLE_PAWNS,
        /**
         * All pawns must move backwards 1 square IF POSSIBLE
         */
        BACK_IT_UP,
        /**
         * Player chooses a square; Any piece that is on that square CANNOT be captured for the duration of the modifier
         */
        SANCTUARY, 
        /**
         * A random knight is chosen (of the side of the player choosing the modifier) and will explode in 10 turns (may change due to balancing)
         */
        EXPLODING_PIECE,
        /**
         * A random bishop is chosen (of the side of the player choosing the modifier). This bishop can move through pieces
         */
        SNIPER_BISHOP,
        /**
         * Player creates a brick on a chosen empty square. No pieces may enter the square or move through it
         */
        BRICK,
        /**
         * Player chooses two squares. At the end of black's turn, the pieces on the portals swap. Replaces any existing portal.
         */
        PORTAL,
        /**
         * Two random files(pieces) swap
         */
        FILE_SWAP,
        /**
         * Player chooses an empty square to place their piece that was last captured 
         */
        RESURRECTION;
        

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
                case PAWNS_ONLY:      return "Pawns Only";
                case INVINCIBLE_PAWNS: return "Invincible Pawns";
                case BACK_IT_UP: return "Pawns Move Backwards";
                case SANCTUARY: return "Sanctuary";
                case EXPLODING_PIECE: return "Mi Bombo";
                case SNIPER_BISHOP:   return "Sniper Bishop";
                case BRICK:           return "Brick an Empty Square";
                case PORTAL:          return "Open two connecting Portals";
                case FILE_SWAP:       return "Flip two random files";
                case RESURRECTION:    return "Resurrect your last lost piece";
                default:              return super.toString();
            }
        }
    }

    private Piece affectedPiece; // used for modifiers that only apply to a single piece
    private int affectedRow;
    private int affectedCol;
    private int turnsRemaining;
    private Type type;

    /**
     * Constructs a modifier that targets a specific piece
     * @param turnsRemaining amt of turns the modifier lasts for
     * @param type the type of modifier
     * @param affectedPiece the affected piece
     */
    public Modifier(int turnsRemaining, Type type, Piece affectedPiece) {
        this.turnsRemaining = turnsRemaining;
        this.type = type;
        this.affectedPiece = affectedPiece;
        this.affectedRow = -1;
        this.affectedCol = -1;
    }

    /**
     * Constructs a modifier that affects a certain type of piece
     * @param remainingTurns amt of turns the modifier lasts for
     * @param type the type of modifier
     */
    public Modifier(int remainingTurns, Type type) {
        this(remainingTurns, type, null);
        this.affectedRow = -1;
        this.affectedCol = -1;
    }

    /**
     * Constructs a modifier that affects a certain square
     * @param turnsRemaining amt of turns remaining
     * @param type the type of modifier
     * @param affectedRow the affected row
     * @param affectedCol the affected col
     */
    public Modifier(int turnsRemaining, Type type, int affectedRow, int affectedCol) {
        this.turnsRemaining = turnsRemaining;
        this.type = type;
        this.affectedRow = affectedRow;
        this.affectedPiece = null;
        this.affectedCol = affectedCol;
    }

    /**
     * gets the type of modifier
     * @return the type of modifier from the enum
     */
    public Type getType() {
        return type;
    }
    
    /**
     * gets the amount of turns remaining on a modifier
     * @return the amount of turns remaining on a modifier
     */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /**
     * Decrements the turns remaining on modifiers
     */
    public void decrementTurns() {
        turnsRemaining--;
    }

    /**
     * Gets the affected piece on a modifier
     * @return the affected piece
     */
    public Piece getAffectedPiece() {
        return affectedPiece;
    }

    /**
     * Gets the affected row
     * @return affected row
     */
    public int getAffectedRow() {
        return affectedRow;
    }    

    /**
     * Gets the affected col
     * @return affected col
     */
    public int getAffectedCol() {
        return affectedCol;
    }

    /**
     * For modifiers that affect a certain class
     * @return the class of pieces affected
     */
    public Class<?> affectedClass() {
        switch (type) {
            case PAWNS_ONLY:   return Pawn.class;
            default:           return null;
        }
    }

    /**
     * Changes the toString() to give a different description for modifiers
     * @param type the type of modifier
     * @return the description
     */
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
            case RESURRECTION: return "Choose a square. Your last captured piece respawns there";
            default: return null;
        }
    }
}
