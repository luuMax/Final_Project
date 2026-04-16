
import javafx.scene.paint.Color;

public class Test {
    public static void main(String[] args) {
        Board board = new Board(Board.BoardType.DEFAULT);
        King wK = new King(Color.WHITE, 7, 7);
        King bK = new King(Color.BLACK, 5, 7);
        Knight k1 = new Knight(Color.WHITE, 0, 1);
        Knight k2 = new Knight(Color.WHITE, 0, 3);
        Knight k3 = new Knight(Color.WHITE, 4, 1);
        Knight k4 = new Knight(Color.WHITE, 4, 3);
        Knight k5 = new Knight(Color.WHITE, 1, 0);
        Knight k6 = new Knight(Color.WHITE, 3, 0);
        Knight k7 = new Knight(Color.WHITE, 1, 4);
        Knight k8 = new Knight(Color.WHITE, 3, 4);
        board.setPieceAt(wK, 7, 7);
        board.setPieceAt(bK, 5, 7);
        board.setPieceAt(k1, 0, 1);
        board.setPieceAt(k2, 0, 3);
        board.setPieceAt(k3, 4, 1);
        board.setPieceAt(k4, 4, 3);
        board.setPieceAt(k5, 1, 0);
        board.setPieceAt(k6, 3, 0);
        board.setPieceAt(k7, 1, 4);
        board.setPieceAt(k8, 3, 4);
        Move move = new Move(0, 1, 2, 2, Move.MoveType.NORMAL, false, false, board);
        System.out.println(move.toString());
        
    }
}

