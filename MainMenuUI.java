import javax.swing.*;
import java.awt.*;
/* import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; */

public class MainMenuUI extends JFrame 
{
    private int windowWidth;
    private int windowLength;

    private static Color fontColor = new Color(214,214,213);


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
        mainpage.setBackground(new Color(36, 34, 32));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 12, 12, 12);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Title Creation
        JLabel titleLeft = new JLabel("Chess", SwingConstants.CENTER);
        titleLeft.setFont(new Font("Sans", Font.BOLD, 80));
        titleLeft.setForeground(new Color(214, 214, 213)); 

        // Start game button creation
        JButton startButton = makeButton("Start", 34, 300, 55, fontColor);
        startButton.addActionListener(e -> {
            Game game = new Game();
            new BoardUI(800, 800, 75, game);
            dispose();
        });

        JButton joinButton = makeButton("Join Game", 34, 300, 55, fontColor);
        joinButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "JoinGame");
        });

        // Settings page accessor button
        JButton settingsButton = makeButton("Settings", 34, 300, 55, fontColor);
        settingsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings");
        });

        c.gridy = 0;
        c.gridx = 0;
        mainpage.add(titleLeft, c);


        c.fill = GridBagConstraints.NONE;
        c.gridy = 1;
        c.gridx = 0;
        mainpage.add(startButton, c);
        
        c.gridy = 2;
        mainpage.add(joinButton, c);

        c.gridy = 3;
        mainpage.add(settingsButton, c);

        JPanel settingsPage = new JPanel(new GridBagLayout());
        settingsPage.setBackground(new Color(36,34,32));

        ImageIcon imagen = new ImageIcon("./PieceSprites/new_knight_white.png");
        Image scaled = imagen.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel horsey1 = new JLabel(new ImageIcon(scaled));

        JLabel snap1 = new JLabel("Aw snap... looks like we didn't make this page yet :(");
        snap1.setFont(new Font("Sans", Font.BOLD, 20));
        snap1.setForeground(new Color(214, 214, 213)); 

        JButton backButton1 = makeButton("<- Back", 34, 300, 55, fontColor);
        backButton1.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainPage");
        });

        c.gridy = 0;
        c.gridx = 0;
        settingsPage.add(horsey1, c);
        c.gridy = 1;
        settingsPage.add(snap1, c);
        c.gridy = 2;
        settingsPage.add(backButton1, c);
        
        JLabel horsey2 = new JLabel(new ImageIcon(scaled));

        JLabel snap2 = new JLabel("Aw snap... looks like we didn't make this page yet :(");
        snap2.setFont(new Font("Sans", Font.BOLD, 20));
        snap2.setForeground(new Color(214, 214, 213)); 
        JPanel joinPage = new JPanel(new GridBagLayout());
        joinPage.setBackground(new Color(36,34,32));
        JButton backButton2 = makeButton("<- Back", 34, 300, 55, fontColor);
        backButton2.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainPage");
        });

        c.gridy = 0;
        c.gridx = 0;
        joinPage.add(horsey2, c);
        c.gridy = 1;
        joinPage.add(snap2, c);
        c.gridy = 2;
        joinPage.add(backButton2, c);



        mainPanel.add(mainpage, "MainPage");
        mainPanel.add(settingsPage, "Settings");
        mainPanel.add(joinPage, "JoinGame");

        add(mainPanel);

        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, "MainPage");

        setVisible(true);
    }

    private JButton makeButton(String name, int size, int width, int height, Color fontColor)
    {
        JButton button = new JButton(name);
        button.setFont(new Font(name, Font.BOLD, size));
        button.setPreferredSize(new Dimension(width, height));
        button.setForeground(fontColor); 
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    public static void main(String[] args) {
        MainMenuUI b = new MainMenuUI(800, 800);
    }
}
