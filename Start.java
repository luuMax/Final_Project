import javax.swing.SwingUtilities;

/**
 * Starts the game.
 */
public class Start // Run Gary Chess
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuUI(800, 800));
    }
}
