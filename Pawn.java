import javafx.scene.paint.Color;public class Pawn extends Piece {
    public Pawn(Color color, int row, int col) {
        super(color, row, col);
    }
    public void move(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        //TODO: implement move for pawn
    }
    public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        //TODO: implement isLegalMove for pawn
        return false;
    }
}
