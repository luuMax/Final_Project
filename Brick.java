import java.awt.*;

/**
 * Represents the brick modifier. Created to prevent other pieces from taking the piece and moving to that square.
 */
public class Brick extends Piece {
    /**
     * Constructs a new Brick object
     * @param color The color of the piece (doesn't matter)
     * @param row The row of the piece
     * @param col The col of the piece
     */
    public Brick(Color color, int row, int col) {
        super(color, row, col);
        setType(Type.BRICK);
    }

    /**
     * Checks if the piece can move to a square legally, as specified in Piece (always false in this case)
     */
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        return false;
    }

    /**
     * used to conform to file naming conventions in this project
     */
    @Override
    public String toString() {
        return "brick";
    }
}