import javafx.scene.paint.Color;;

public class Game {
    private Board board;
    private Color currentTurn;
    private boolean gameState; // true means ongoing, false means game over

    public Game() {
        board = new Board(Board.BoardType.DEFAULT);
        board.initializePieces(Board.BoardType.DEFAULT);
        currentTurn = Color.WHITE;
        gameState = true; 
    }

    

}
