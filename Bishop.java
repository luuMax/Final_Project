import javafx.scene.paint.Color;

public class Bishop extends Piece
{
    public Bishop(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: Bishop moving logic
        return false;
    }

}
