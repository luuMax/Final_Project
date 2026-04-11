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


    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        // TODO: implement isLegalMove for pawn
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
        
        // TODO: captures (diagonal) 
        if (toRow - fromRow == direction && Math.abs(toCol - fromCol) == 1 && board.getPieceAt(toRow, toCol) != null && getColor() != board.getPieceAt(toRow, toCol).getColor()) {
            return true;
        }



        //Promotion is also a special case
        //move will handle promotion not isLegalMove
        // TODO: en passant? //lol
        return false;
    }
}
