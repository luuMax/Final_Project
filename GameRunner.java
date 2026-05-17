import java.awt.*;

public class GameRunner {
    private Game game;
    private BoardUI boardUI;
    private NetworkManager network;
    private Color localColor;

    public GameRunner(NetworkManager network) {
        this.network    = network;
        //this.localColor = network.getIsWhite() ? Color.WHITE : Color.BLACK;
    }

    // Keep this so you can still run locally without networking for testing
    public static void main(String[] args) {
        new GameRunner(null).start();
    }

    public void start() {
        game    = new Game();
        boardUI = new BoardUI(800, 800, 75, game);

        // If there's no network (local testing), nothing else to do
        if (network == null) return;

        // Background thread: blocks waiting for opponent moves, applies them when they arrive
        Thread listenerThread = new Thread(() -> {
            while (!game.isGameOver()) {
                int[] move = network.receiveMove(); // blocks here until opponent sends something

                if (move == null) {
                    System.out.println("Opponent disconnected.");
                    break;
                }

                int fr = move[0], fc = move[1], tr = move[2], tc = move[3];

                // Touch the UI only from the Swing thread
                javax.swing.SwingUtilities.invokeLater(() -> {
                    boolean valid = game.makeMove(fr, fc, tr, tc);
                    if (valid) {
                        boardUI.redrawBoard();
                        if (game.isGameOver()) {
                            System.out.println("Game over!");
                        }
                    } else {
                        System.out.println("WARNING: received invalid move " + fr + "," + fc + " -> " + tr + "," + tc);
                    }
                });
            }
            network.close();
        });

        listenerThread.setDaemon(true); //on disconnect
        listenerThread.start();
    }
}