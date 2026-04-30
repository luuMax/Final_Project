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


        System.out.println(game.getCurrentTurn()); //testing;
    }

}
