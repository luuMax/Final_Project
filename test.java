public class test {
    public static void main(String[] args) {
        Board board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces(Board.BoardType.DEFAULT);
        Move move = new Move(board.getPieceAt(6, 4), null, 6, 4, 4, 4, Move.MoveType.NORMAL, false, false);
        System.out.println(move.toString());
    }
}
