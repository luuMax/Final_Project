import javafx.scene.paint.Color;

public class King extends Piece
{  
    boolean isFirstMove = true;
    public King(Color color, int row, int col)
    {
        super(color, row, col);
    }



    // just for king so theres no infinite loop in isInCheck()
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8 && Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1) {
            if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() != this.getColor()) {
                return true;
            }
            else if (board.getPieceAt(toRow, toCol) == null) {
                return true;
            }
        }
        return false;
    }

    // Used for check, checkmate, pin logic
    public boolean isInCheck(Color color, Board board) {
        King king = board.getKing(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getColor() != color && piece.canMoveTo(row, col, king.getRow(), king.getCol(), board)) {
                    return true;
                }
            }
        }
        return false;
    }
}
