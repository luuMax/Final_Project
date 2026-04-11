import javafx.scene.paint.Color;

public class King extends Piece
{  
    boolean isFirstMove = true;
    public King(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement move for king

        //there is 8 adja. squares + castling (we can ignore for now?)
        //also queenside castling
    }


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement isLegalMove for king
        return false;
    }

    // Used for check, checkmate, pin logic
    public boolean isInCheck(Color color, Board board) {
        King king = board.getKing(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPieceAt(row, col) != null && board.getPieceAt(row, col).isLegalMove(row, col, king.getRow(), king.getCol(), board)) {
                    return true;
                }
            }
        }
        return false;
    }
}
