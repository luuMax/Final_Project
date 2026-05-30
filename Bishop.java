import java.awt.*;

/**
 * Represents the Bishop piece in chess. Has purely diagonal movement.
 */
public class Bishop extends Piece
{
    /**
     * Constructs a new Bishop object
     * @param color The side of the piece
     * @param row the row of the piece
     * @param col the col of the piece
     */
    public Bishop(Color color, int row, int col)
    {
        super(color, row, col);
        setType(Type.BISHOP);
    }

    /**
     * Checks if the piece can move to a square legally, as specified in Piece
     */
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() == this.getColor()) {
            return false;
        }
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8 && 
            (fromRow != toRow && fromCol != toCol) &&
            Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol)) {
            for (Modifier m : board.getActiveModifiers()) {
                if (m.getType() == Modifier.Type.SNIPER_BISHOP && m.getAffectedPiece().equals(this)) {
                    return true;
                }
            }
            if (isPathClear(fromRow, fromCol, toRow, toCol, board)) {
                return true;
            }
        }
        return false;
    }

}
