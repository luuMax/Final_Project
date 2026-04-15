import javafx.scene.paint.Color;public class Queen extends Piece {
    public Queen(Color color , int row, int col) {
        super(color, row, col);
    }
    public boolean canMoveTo(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        //TODO: Queen moving logic
        return false;
    }
}
