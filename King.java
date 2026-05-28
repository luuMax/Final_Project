import java.awt.*;

public class King extends Piece
{  
    /**
     * Constructs a new King object
     * @param color The color of the piece
     * @param row The row of the piece
     * @param col The col of the piece
     */
    public King(Color color, int row, int col)
    {
        super(color, row, col);
        setType(Type.KING);
    }

    /**
     * Checks if the piece can move to a square legally, as specified in Piece
     */
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (toRow >= 0 && toRow < 8 && toCol >= 0 && toCol < 8 && Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1) {
            if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() != this.getColor()) {
                return true;
            }
            else if (board.getPieceAt(toRow, toCol) == null) {
                return true;
            }
        }
        // castling
        else if (toRow == fromRow && isFirstMove && Math.abs(toCol-fromCol) == 2) {
            int dir = (int) Math.signum(toCol - fromCol); //short vs long castle direction
            Piece piece;
            if (dir == 1) {
                if (isPathClear(fromRow, fromCol, toRow, 7, board)) {
                    piece = board.getPieceAt(toRow, 7);
                }
                else {
                    return false;
                }
                
            }
            else {
                if (isPathClear(fromRow, fromCol, toRow, 0, board)) {
                    piece = board.getPieceAt(toRow, 0);
                }
                else {
                    return false;
                }
            }
            if (piece instanceof Rook && piece.isFirstMove) {
                return true;
            }
        }
        return false;
    }
}
