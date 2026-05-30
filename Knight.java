import java.awt.*;

/**
 * Represents the Knight piece. Can move in "L" patterns
 */
public class Knight extends Piece
{  
    /**
     * Constructs a new Knight object
     * @param color The color of the piece
     * @param row The row of the piece
     * @param col The col of the piece
     */
    public Knight(Color color, int row, int col)
    {
        super(color, row, col);
        setType(Type.KNIGHT);
    }

    /**
     * Checks if the piece can move to a square legally, as specified in Piece
     */
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
