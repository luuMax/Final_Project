public class GameRunner //basically gamerunner, boardui, and modifiers are all thats left (true)
{
    private Game game;
    private BoardUI boardUI;

    public static void main(String[] args) {
        GameRunner runner = new GameRunner();
        runner.start();
    }
    //Neel can you add a boba and a booba emote please
    
    //testing, currently boardui has no references to game
    public void start() {
        game = new Game();
        boardUI = new BoardUI(800, 800, 30, game); 
    }

}
