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
        this.localColor =
            network != null ? (network.getIsWhite() ? Color.WHITE : Color.BLACK) : null;
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
                boardUI = new BoardUI(800, 800, 75, game, network, localColor);

                Thread listenerThread = new Thread(() -> {
                    while (!game.isGameOver())
                    {
                        int[] move = network.receiveMove();

                        if (move == null)
                        {
                            System.out.println("Opponent disconnected.");
                            break;
                        }

                        int fr = move[0], fc = move[1], tr = move[2], tc = move[3];

                        boolean valid = game.makeMove(fr, fc, tr, tc);

                        if (!valid)
                        {
                            System.out.println(
                                "WARNING: invalid move received " + fr + "," + fc + " -> " + tr
                                    + "," + tc);
                            continue;
                        }

                        javax.swing.SwingUtilities.invokeLater(() -> {
                            boardUI.redrawBoard();
                            if (game.isGameOver())
                                boardUI.endGame();
                        });

                        if (game.shouldOfferModifier())
                        {
                            // discard options — mover already picked
                            network.receiveModifierOptions();

                            // receive the fully serialized modifier
                            NetworkManager.ModifierData data = network.receiveModifier();

                            if (data != null)
                            {
                                // reconstruct the Modifier — only wiring, no
                                // game rules
                                Modifier m;
                                if (data.pieceRow != -1 && data.pieceCol != -1)
                                {
                                    // piece-targeted modifier — look up piece
                                    // on our board
                                    Piece piece =
                                        game.getBoard().getPieceAt(data.pieceRow, data.pieceCol);
                                    m = new Modifier(data.turnsRemaining, data.type, piece);
                                }
                                else if (data.affectedRow != -1 && data.affectedCol != -1)
                                {
                                    // square-targeted modifier
                                    m = new Modifier(
                                        data.turnsRemaining,
                                        data.type,
                                        data.affectedRow,
                                        data.affectedCol);
                                }
                                else
                                {
                                    // board-wide modifier
                                    m = new Modifier(data.turnsRemaining, data.type);
                                }

                                game.addModifier(m);

                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    boardUI.redrawBoard();
                                });
                            }
                        }
                    }
                    network.close();
                });

                listenerThread.setDaemon(true);
                listenerThread.start();

            }
            else
            {
                boardUI = new BoardUI(800, 800, 75, game);
            }
        });
    }
}
