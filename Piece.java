import java.util.ArrayList;
import java.awt.*;
// used javafx color instead of awt color to avoid conflicts with
// javafx.scene.paint.Color in Board.java

public abstract class Piece
{

    public enum Type{PAWN, KING, KNIGHT, ROOK, QUEEN, BISHOP, BRICK, PORTAL};
    public enum Side{WHITE, BLACK};

    protected boolean isFirstMove = true;

    private Color color;
    private int row;
    private int col;
    private Type type;
    private Side side;

    /**
     * Constructs a new Piece object
     * @param color the Color of the Piece (Which player the piece belongs to)
     * @param row the row of the piece
     * @param col the col of the piece
     */
    public Piece(Color color, int row, int col)
    {
        this.color = color;
        if(this.color.equals(Color.BLACK))
        {
            side = Side.BLACK;
        }
        else
        {
            side = Side.WHITE;
        }
        this.row = row;
        this.col = col;
    }

    /**
     * returns the type of the piece
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Sets the type of the piece
     * @param type the type of the piece (one of the enum states)
     */
    public void setType(Type type)
    {
        this.type = type;
    }
    
    /**
     * Gets the side of the piece 
     * @return the side
     */
    public Side getSide()
    {
        return side;
    }



    // needs board[][] to check if move is legal, since new moves change current
    // board
    // before returning true, make the move temporarily and then call isInCheck, and undo the move if necessary
    // assumes that the toRow, toCol is a square on the board

    // checks if the piece can move to the square, disregarding possible checks on the king
    /**
     * Checks if a piece can move to a given square
     * @param fromRow starting row of the piece
     * @param fromCol starting col of the piece
     * @param toRow target row of the piece
     * @param toCol target col of the piece
     * @param board the board
     * @return true if the move is legal, false if not
     */
    public abstract boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board);

    // Get legal moves, can display all legal moves like in chess.com. O(64).
    // 8x8 board.
    /**
     * Gets all legal moves in the position
     * @param board the board
     * @return an ArrayList<String> of all squares where pieces can legally move to
     */
    public ArrayList<String> getLegalMoves(Board board)
    {
        ArrayList<String> legalMoves = new ArrayList<>();
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                if (canMoveTo(this.row, this.col, r, c, board))
                {
                    legalMoves.add(r + "," + c);
                }
            }
        }
        return legalMoves;
    }


    // helper method for bishop, rook, queen
    /**
     * Helper method for Bishops, Rooks, Queens, which checks if the path is clear to move through
     * @param fromRow the starting row of the piece
     * @param fromCol the starting col of the piece
     * @param toRow the ending row of the piece
     * @param toCol the ending col of the piece
     * @param board the board
     * @return true if the path is clear, false otherwise
     */
    public boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol, Board board)
    {
        int rowStep = (int)Math.signum(toRow - fromRow);
        int colStep = (int)Math.signum(toCol - fromCol);

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;
        while (currentRow != toRow || currentCol != toCol)
        {
            if (board.getPieceAt(currentRow, currentCol) != null)
            {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    /**
     * Gets the color of a piece
     * @return the Color of the piece
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Gets the row of the piece
     * @return the row of the piece
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Gets the col of the piece
     * @return the col of the piece
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Sets the color of a piece
     * @param color the Color
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Sets the row of a piece
     * @param row the row
     */
    public void setRow(int row)
    {
        this.row = row;
    }

    /**
     * Sets the col of a piece
     * @param col the col
     */
    public void setCol(int col)
    {
        this.col = col;
    }
    
    /**
     * Prints out the Piece as a String
     */
    public String toString()
    {
        return (getType() + "_" + getSide()).toLowerCase();
    }
}
