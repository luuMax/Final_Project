import javafx.scene.paint.Color;

public class Knight extends Piece
{
    public Knight(Color color, int row, int col)
    {
        super(color, row, col);
    }

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        // TODO: Knight moving logic
        return false;
    }
}
