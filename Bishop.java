import javafx.scene.paint.Color;

public class Bishop extends Piece
{
    public Bishop(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8 && 
            (fromRow != toRow && fromCol != toCol) &&
            Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol) && 
            isPathClear(fromRow, fromCol, toRow, toCol, board)) {
            return true;
        }
        return false;
    }

}
