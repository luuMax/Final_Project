public class GameRunner //basically gamerunner, boardui, and modifiers are all thats left (lie)
{
    private Game game;
    private BoardUI boardUI;

    public static void main(String[] args) {
        GameRunner runner = new GameRunner();
        runner.start();
    }

    //testing, currently boardui has no references to game
    public void start() {
        game = new Game();
        boardUI = new BoardUI(600, 600, 75); 
        boardUI.initialize();


                //just random tests rn
        System.out.println(game.getCurrentTurn());
        Board imagineNamingYourSonBoard2 = new Board();
        for (Piece[] pieceRow : imagineNamingYourSonBoard2.getBoard()) {
            for (Piece piece : pieceRow) {
                if (piece != null) {
                    char file = (char) ('a' + piece.getCol());
                    int rank = 8 - piece.getRow();
                    String currentNotation = "" + file + rank;
                    String n;

                    switch (piece) {
                        case King _ -> {n= "King";}
                        case Queen _ -> {n = "Queen";}
                        case Rook _ -> {n = "Rook";}
                        case Knight _ -> {n = "Knight";}
                        case Pawn _ -> {n = "Pawn";}
                        case Bishop _ -> {n = "Bishop";}
                        default -> {n = "Nothing";}
                    }

                    

                    System.out.println(n + " at " + currentNotation);
                }
            }
        }
    }

}
