import java.awt.*;

public class GameRunner {
    private Game game;
    private BoardUI boardUI;
    private NetworkManager network;
    private Color localColor;

    public GameRunner(NetworkManager network) {
        this.network    = network;
        this.localColor = network != null ? (network.getIsWhite() ? Color.WHITE : Color.BLACK) : null;
    }

    public static void main(String[] args) {
        new GameRunner(null).start();
    }

    public void start() {
        game = new Game();

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (network != null) {
                boardUI = new BoardUI(800, 800, 75, game, network, localColor);

                Thread listenerThread = new Thread(() -> {
                    while (!game.isGameOver()) {
                        int[] move = network.receiveMove();

                        if (move == null) {
                            System.out.println("Opponent disconnected.");
                            break;
                        }

                        int fr = move[0], fc = move[1], tr = move[2], tc = move[3];

                        // Apply move directly on listener thread — no EDT needed for state
                        boolean valid = game.makeMove(fr, fc, tr, tc);

                        if (!valid) {
                            System.out.println("WARNING: invalid move received "
                                + fr + "," + fc + " -> " + tr + "," + tc);
                            continue;
                        }

                        // Check for modifier BEFORE handing off to EDT
                        if (game.shouldOfferModifier()) {
                            Modifier.Type[] options = network.receiveModifierOptions();
                            if (options != null) {
                                final Modifier.Type[] finalOptions = options;
                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    boardUI.redrawBoard();
                                    boardUI.showMods(finalOptions);
                                });
                            }
                        } else {
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                boardUI.redrawBoard();
                                if (game.isGameOver()) boardUI.endGame();
                            });
                        }
                    }
                    network.close();
                });

                listenerThread.setDaemon(true);
                listenerThread.start();

            } else {
                boardUI = new BoardUI(800, 800, 75, game);
            }
        });
    }
}