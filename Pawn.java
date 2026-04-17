import javafx.scene.paint.Color;

public class Pawn extends Piece
{
    protected boolean isEnPassantable = false;
    public Pawn(Color color, int row, int col)
    {
        super(color, row, col);
    }

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int direction = (this.getColor() == Color.WHITE) ? -1 : 1; // different directions for white and black
        // forward moves
        // move up 2
        if (toRow - fromRow == 2 * direction && isFirstMove && toCol - fromCol == 0 && board.getPieceAt(fromRow + direction, fromCol) == null && board.getPieceAt(fromRow + 2 * direction, fromCol) == null) {
            return true;
        }

        // move up 1
        if (toRow - fromRow == direction && toCol - fromCol == 0 && board.getPieceAt(fromRow + direction, fromCol) == null) {
            return true;
        }
        
        if (toRow - fromRow == direction && Math.abs(toCol - fromCol) == 1 && board.getPieceAt(toRow, toCol) != null && getColor() != board.getPieceAt(toRow, toCol).getColor()) {
            return true;
        }

        // TODO: en passant
        if (toRow - fromRow == direction && Math.abs(toCol - fromCol) == 1 && board.getPieceAt(fromRow, toCol) instanceof Pawn && ((Pawn) board.getPieceAt(fromRow, toCol)).isEnPassantable) {
            return true;
        }
        return false;
    }
}
