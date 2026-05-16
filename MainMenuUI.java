import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

public class MainMenuUI extends JFrame 
{
    private int windowWidth;
    private int windowLength;


    public MainMenuUI(int windowW, int windowL)
    {
        windowWidth = windowW;
        windowLength = windowL;
        
        initialize();
    }

    public void initialize()
    {
        setTitle("The Menu");
        setSize(windowLength,windowWidth);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        JPanel mainpage = new JPanel(new GridBagLayout());
        mainpage.setBackground(new Color(245, 235, 220));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Chess(with modifiers)", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 52));

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(220, 55));
        startButton.setFocusPainted(false);

        startButton.addActionListener(e -> {
            Game game = new Game();
            new BoardUI(800, 800, 60, game);
            dispose();
        });

        JButton joinButton = new JButton("Join Game");
        joinButton.setFont(new Font("Arial", Font.BOLD, 20));
        joinButton.setPreferredSize(new Dimension(220, 55));
        joinButton.setFocusPainted(false);

        joinButton.addActionListener(e -> {
            Game game = new Game();
            new BoardUI(800, 800, 60, game);
            dispose();
        });

        c.gridy = 0;
        mainpage.add(title, c);

        c.gridy = 1;
        mainpage.add(startButton, c);
        
        c.gridy = 2;
        mainpage.add(joinButton, c);

        mainPanel.add(mainpage, "Login");

        add(mainPanel);

        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, "Login");

        setVisible(true);
    }

    public static void main(String[] args) {
        MainMenuUI b = new MainMenuUI(800, 800);
    }
}
