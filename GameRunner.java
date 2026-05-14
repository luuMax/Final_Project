import java.awt.*;

public class GameRunner {
    private Game game;
    private BoardUI boardUI;
    private NetworkManager network;
    private Color localColor;

    public GameRunner(NetworkManager network) {
        this.network    = network;
        this.localColor = network != null ? (network.getIsWhite() ? Color.WHITE : Color.BLACK) : Color.WHITE;
    }

    public static void main(String[] args) {
        new GameRunner(null).start();
    }

    public void start() {
        game = new Game();

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (network != null) {
                boardUI = new BoardUI(800, 800, 100, game, network, localColor);

                Thread listenerThread = new Thread(() -> {
                    while (!game.isGameOver()) {
                        int[] move = network.receiveMove();

                        if (move == null) {
                            System.out.println("Opponent disconnected.");
                            break;
                        }

<<<<<<< HEAD
                        int fr = move[0], fc = move[1], tr = move[2], tc = move[3];
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            boolean valid = game.makeMove(fr, fc, tr, tc);
                            if (valid) {
                                boardUI.redrawBoard();
                            } else {
                                System.out.println("WARNING: invalid move received "
                                    + fr + "," + fc + " -> " + tr + "," + tc);
                            }
                        });
                    }
                    network.close();
                });

                listenerThread.setDaemon(true);
                listenerThread.start();

            } else {
                boardUI = new BoardUI(800, 800, 100, game);
            }
        });
=======
        listenerThread.setDaemon(true); //on disconnect
        listenerThread.start();
>>>>>>> af0e339faa61080cd9a2597e185f20571ac01fdb
    }
}