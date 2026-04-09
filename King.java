import javafx.scene.paint.Color;public class King extends Piece {
    public King(Color color, int row, int col) {
        super(color, row, col);
    }
    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        //TODO: implement move for king
    }
    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        //TODO: implement isLegalMove for king
        return false;
    }
}
