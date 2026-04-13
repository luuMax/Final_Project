import javafx.scene.paint.Color;

public class Bishop extends Piece
{
    public Bishop(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement move for bishop

    }


    public boolean isAttacking(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        return false;
    }

    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement isLegalMove for bishop
        return false;
    }

}
