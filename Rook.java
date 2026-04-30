import java.awt.*;

public class Rook extends Piece
{
    boolean isFirstMove = true;
    public Rook(Color color, int row, int col)
    {
        super(color, row, col);
    }

    //should we use the piece move method? 
    /* public void move(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        
    } */


    // isLegalMove checks if piece follows its movement rules, doesn't jump over
    // pieces, doesn't capture friendly piece

    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
         // Rook moves in straight lines, can't jump over pieces

        if (fromRow != toRow && fromCol != toCol)
        {
            return false;
        }
        // moving horizontally, check all squares in between
        if (fromRow == toRow)
        {
            int minCol = Math.min(fromCol, toCol);
            int maxCol = Math.max(fromCol, toCol);
            for (int c = minCol + 1; c < maxCol; c++)
            {
                if (board.getPieceAt(fromRow, c) != null)
                {
                    return false;
                }
            }
        }
        else
        { // moving vertically, check all squares in between
            int minRow = Math.min(fromRow, toRow);
            int maxRow = Math.max(fromRow, toRow);
            for (int r = minRow + 1; r < maxRow; r++)
            {
                if (board.getPieceAt(r, fromCol) != null)
                {
                    return false;
                }
            }
        }

        if (board.getPieceAt(toRow, toCol) != null && board.getPieceAt(toRow, toCol).getColor().equals(this.getColor()))
        {
            return false; // can't capture own piece
        }

        return true;
    }
}
