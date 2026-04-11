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


    public abstract void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board);


    public abstract
        boolean
        isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board);


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
