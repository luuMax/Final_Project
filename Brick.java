import java.awt.*;

public class Brick extends Piece {
    public Brick(Color color, int row, int col) {
        super(color, row, col);
        setType(Type.BRICK);
    }

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        return false;
    }

    @Override
    public String toString() {
        return "brick";
    }
}