import java.awt.Color;
public abstract class Piece {
    Color color;
    public Piece(Color color) {
        this.color = color;
    }
    public abstract void move();

    public abstract boolean isLegalMove();
    
}
