import java.util.ArrayList;
import javafx.scene.paint.Color;
// used javafx color instead of awt color to avoid conflicts with
// javafx.scene.paint.Color in Board.java

public abstract class Piece
{
    private Color color;
    private int   row;
    private int   col;

    public Piece(Color color, int row, int col)
    {
        this.color = color;
        this.row = row;
        this.col = col;
    }


    // needs board[][] to check if move is legal, since new moves change current
    // board
    public abstract
        boolean
        isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board);


    // move can be nonabstract.. can just be the smae for all pieces as its just
    // a shift in position.
    // game logic & islegalmove will handle capturing, enpassant, castling..
    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        board[toRow][toCol] = this;
        board[fromRow][fromCol] = null;
        this.setRow(toRow);
        this.setCol(toCol);
    }


    // Get legal moves, can display all legal moves like in chess.com. O(64).
    // 8x8 board.
    public ArrayList<String> getLegalMoves(Piece[][] board)
    {
        ArrayList<String> legalMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                if (isLegalMove(this.row, this.col, r, c, board))
                {
                    legalMoves.add(r + "," + c);
                }
            }
        }
        return legalMoves;
    }


    // helper method for bishop, rook, queen
    public boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board)
    {
        int rowStep = (int)Math.signum(toRow - fromRow);
        int colStep = (int)Math.signum(toCol - fromCol);

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;
        while (currentRow != toRow || currentCol != toCol)
        {
            if (board[currentRow][currentCol] != null)
            {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }


    public Color getColor()
    {
        return color;
    }


    public int getRow()
    {
        return row;
    }


    public int getCol()
    {
        return col;
    }


    public void setColor(Color color)
    {
        this.color = color;
    }


    public void setRow(int row)
    {
        this.row = row;
    }


    public void setCol(int col)
    {
        this.col = col;
    }
}
