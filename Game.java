import javafx.scene.paint.Color;;

public class Game {
    private Piece[][] board;
    private Color currentTurn;
    private boolean gameState;

    public Game() {
        board = new Piece[8][8];
        currentTurn = Color.WHITE;
        gameState = true; // true means game is ongoing, false means game is over
        
    }

}
