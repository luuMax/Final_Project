import java.awt.*;

public class Queen extends Piece {
    /**
     * Creates a new Queen object
     * @param color The color of the piece
     * @param row The row of the piece
     * @param col The col of the piece
     */
    public Queen(Color color , int row, int col) {
        super(color, row, col);
        setType(Type.QUEEN);
    }

    /**
     * Checks if the piece can move to a square legally, as specified in Piece
     */
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor() == this.getColor()) {
            return false;
        }
        if (Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol) && isPathClear(fromRow, fromCol, toRow, toCol, board)) {
            return true;
        }  
        else if (((toRow - fromRow) != 0 && (toCol - fromCol) == 0 ) && isPathClear(fromRow, fromCol, toRow, toCol, board) || ((toRow - fromRow) == 0 && (toCol - fromCol) != 0) && isPathClear(fromRow, fromCol, toRow, toCol, board)) {
            return true;
        }
        return false;
    }
}
