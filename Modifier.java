public class Modifier {
    public enum Type {
        // BRAINSTORM 
        PAWNS_ONLY, KNIGHTS_ONLY, BISHOPS_ONLY, ROOKS_ONLY, QUEENS_ONLY, KINGS_ONLY, EXPLODING_PIECE
    }
    int turnsRemaining;
    private Type type;

    public Modifier(int turns, Type type) {
        turnsRemaining = turns;
    }

    public Type getType() {
        return type;
    }
    
    public int getTurnsRemaining() {
        return turnsRemaining;
    }
}
