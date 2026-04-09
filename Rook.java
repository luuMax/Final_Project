import javafx.scene.paint.Color;

public class Rook
    extends Piece
{
    public Rook(Color color, int row, int col)
    {
        super(color, row, col);
    }


    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        board[toRow][toCol] = this; //if there was a piece there, it's captured/replaced
        board[fromRow][fromCol] = null; 
        this.row = toRow;
        this.col = toCol;
    }

    //isLegalMove checks if piece follows its movement rules, doesn't jump over pieces, doesn't capture friendly piece
    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        //Rook moves in straight lines, can't jump over pieces

        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        //moving horizontally, check all squares in between
        if (fromRow == toRow) {
            int minCol = Math.min(fromCol, toCol);
            int maxCol = Math.max(fromCol, toCol);
            for (int c = minCol + 1; c < maxCol; c++) {
                if (board[fromRow][c] != null) {
                    return false;
                }
            }
        }
        else { // moving vertically, check all squares in between
            int minRow = Math.min(fromRow, toRow);
            int maxRow = Math.max(fromRow, toRow);
            for (int r = minRow + 1; r < maxRow; r++) {
                if (board[r][fromCol] != null) {
                    return false;
                }
            }
        }

        if (board[toRow][toCol] != null && board[toRow][toCol].color.equals(this.color)) {
            return false; // can't capture own piece
        }

        return true;
    }
}
