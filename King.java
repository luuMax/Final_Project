import javafx.scene.paint.Color;

public class King extends Piece
{
    public King(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement move for king

        //there is 8 adja. squares + castling (we can ignore for now?)
        //also queenside castling
    }


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        // TODO: implement isLegalMove for king
        return false;
    }

    // Used for check, checkmate, pin logic
    public boolean isInCheck(Color color, Piece[][] board) {
        // TODO: implement isInCheck for king
        // loop through every piece on board and use isLegalMove() with the king's position
        // if any can move to the king return true
        return false;
    }
}
