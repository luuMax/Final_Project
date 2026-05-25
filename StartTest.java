import javax.swing.SwingUtilities;

public class StartTest //2 program tester
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuUI(800, 800));
    }
}
