import javafx.scene.paint.Color;

public class Knight extends Piece
{
    public Knight(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement move for knight
    }


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement isLegalMove for knight
        return false;
    }
}
