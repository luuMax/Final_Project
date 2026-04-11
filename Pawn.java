import javafx.scene.paint.Color;

public class Pawn extends Piece
{
    boolean isFirstMove = true;
    public Pawn(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement move for pawn
    }


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement isLegalMove for pawn
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1; // different directions for white and black
        // forward moves

        // move up 2
        if (Math.abs(toRow - fromRow) == 2 && isFirstMove && toCol - fromCol == 0 && board[fromRow + direction][fromCol] == null && board[fromRow + 2 * direction][fromCol] == null) {
            return true;
        }

        if (Math.abs(toRow - fromRow) == 1 && toCol - fromCol == 0 && board[fromRow + direction][fromCol] == null) {
            return true;
        }
        

        // TODO: captures (diagonal) 

        //Promotion is also a special case
        // TODO: en passant? //lol
        return false;
    }
}
