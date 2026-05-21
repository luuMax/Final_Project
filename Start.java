import javax.swing.SwingUtilities;

public class Start //Run Gary Chess
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuUI(800, 800));
    }
}
