public class Board {
    private BoardType boardType;
    private Piece[][] boardArr;

    private King whiteKing;
    private King blackKing;

    public enum BoardType {
        DEFAULT, CUSTOM
    }

    public Board(BoardType boardType) {
        this.boardType = boardType;
        this.boardArr = new Piece[8][8];
        
    }

    public Board() {
        this(BoardType.DEFAULT);
    }

    // public void initializePieces(BoardType boardType) {
    //     if (this.boardType == boardType) {
    //         // initialize pieces in starting positions
    //     }
    //     else {
    //         // do other stuff for other modes maybe
    //     }
    // }
}