import java.awt.*;

public class GameRunner
{

    private Game           game;
    private BoardUI        boardUI;
    private NetworkManager network;
    private Color          localColor;

    public GameRunner(NetworkManager network)
    {

        this.network = network;

        if (network != null)
        {

            this.localColor = network.getIsWhite() ? Color.WHITE : Color.BLACK;

        }
        else
        {

            this.localColor = Color.WHITE;
        }
    }


    public static void main(String[] args)
    {

        new GameRunner(null).start();
    }


    public void start()
    {

        game = new Game();

        javax.swing.SwingUtilities.invokeLater(() -> {

            if (network != null)
            {

                boardUI = new BoardUI(800, 800, 100, game, network, localColor);

                Thread listenerThread = new Thread(() -> {

                    while (!game.isGameOver())
                    {

                        int[] move = network.receiveMove();

                        if (move == null)
                        {

                            System.out.println("Opponent disconnected.");

                            break;
                        }

                        int fr = move[0];
                        int fc = move[1];
                        int tr = move[2];
                        int tc = move[3];

                        javax.swing.SwingUtilities.invokeLater(() -> {

                            boolean valid = game.makeMove(fr, fc, tr, tc);

                            if (valid)
                            {

                                boardUI.redrawBoard();

                            }
                            else
                            {

                                System.out.println(
                                    "WARNING: invalid move received " + fr + "," + fc + " -> " + tr
                                        + "," + tc);
                            }
                        });
                    }

                    network.close();
                });

                listenerThread.setDaemon(true);

                listenerThread.start();

            }
            else
            {

                boardUI = new BoardUI(800, 800, 100, game);
            }
        });
    }
}
