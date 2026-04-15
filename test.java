public class Test {
    public static void main(String[] args) {
        Board board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces(Board.BoardType.DEFAULT);
        Move move = new Move(board.getPieceAt(6, 4), null, 6, 4, 4, 4, Move.MoveType.NORMAL, false, false);
        System.out.println(move.toString());
        Move move2 = new Move(board.getPieceAt(7, 1), null, 7, 1, 5, 2, Move.MoveType.NORMAL, false, false);
        System.out.println(move2.toString());
    }
}
