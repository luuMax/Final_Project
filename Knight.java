import java.awt.*;

public class Knight extends Piece
{
    public Knight(Color color, int row, int col)
    {
        super(color, row, col);
    }

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8) {
            if (Math.abs(toRow - fromRow) == 2 && Math.abs(toCol - fromCol) == 1 && (board.getPieceAt(toRow, toCol) == null || board.getPieceAt(toRow, toCol).getColor() != getColor())) {
                return true;
            }
            else if (Math.abs(toRow - fromRow) == 1 && Math.abs(toCol - fromCol) == 2 && (board.getPieceAt(toRow, toCol) == null || board.getPieceAt(toRow, toCol).getColor() != getColor())) {
                return true;
            }
        }
        return false;
    }
}
