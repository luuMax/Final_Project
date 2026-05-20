import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuUI extends JFrame 
{
    private int windowWidth;
    private int windowLength;
    private static final int preferredTileSize = 75;

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

        //////////////////////
        // Title Creation   //
        //////////////////////
        JLabel titleLeft = new JLabel("Chess", SwingConstants.CENTER);
        titleLeft.setFont(new Font("Sans", Font.BOLD, 80));
        titleLeft.setForeground(new Color(214, 214, 213)); 

        //////////////////////////////////
        // Main page buttons creation   //
        //////////////////////////////////
        JButton startButton = makeButton("Start", 34, 300, 55, fontColor);
        startButton.addActionListener(e -> {
            Game g = new Game();
            BoardUI b = new BoardUI(800, 800, preferredTileSize, g);
            dispose();
        });

        JButton hostButton = makeButton("Host Game", 34, 300, 55, fontColor);
        hostButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "HostGame");
        });

        JButton joinButton = makeButton("Join Game", 34, 300, 55, fontColor);
        joinButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "JoinGame");
        });

        c.gridx = 0;
        c.gridy = 0;
        mainpage.add(titleLeft, c);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 1;
        c.gridx = 0;
        mainpage.add(startButton, c);
        c.gridy = 2;
        c.gridx = 0;
        mainpage.add(hostButton, c);
        c.gridy = 3;
        mainpage.add(joinButton, c);

        ImageIcon image = new ImageIcon("./PieceSprites/new_knight_white.png");
        Image scaled = image.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);

        //////////////////////////
        // Making the host page //
        //////////////////////////
        JLabel horsey1 = new JLabel(new ImageIcon(scaled));
        JPanel hostPage = new JPanel(new GridBagLayout());
        hostPage.setBackground(new Color(36,34,32));

        JLabel yay = new JLabel("Game created.");
        yay.setFont(new Font("Sans", Font.BOLD, 20));
        yay.setForeground(new Color(214, 214, 213)); 
        JLabel gameCode = new JLabel("Game Code(share with other player):");
        gameCode.setFont(new Font("Sans", Font.BOLD, 20));
        gameCode.setForeground(new Color(214, 214, 213)); 
        JLabel gameCode1 = new JLabel(GameCode.encode(GameCode.getIpAddress()));
        gameCode1.setFont(new Font("Sans", Font.BOLD, 30));
        gameCode1.setForeground(new Color(214, 214, 213)); 

        c.gridx = 0;
        c.gridy = 0;
        hostPage.add(yay, c);
        c.gridx = 0;
        c.gridy = 1;
        hostPage.add(horsey1, c);
        c.gridx = 0;
        c.gridy = 2;
        hostPage.add(gameCode, c);
        c.gridx = 0;
        c.gridy = 3;
        hostPage.add(gameCode1, c);

        //////////////////////////
        // Join page creation   //
        //////////////////////////
        JLabel horsey2 = new JLabel(new ImageIcon(scaled));
        JLabel prompt = new JLabel("Enter gamelink here:");
        prompt.setFont(new Font("Sans", Font.BOLD, 20));
        prompt.setForeground(new Color(214, 214, 213)); 
        JPanel codePanel = new JPanel(new FlowLayout());
        codePanel.setOpaque(false);
        JTextField gameCodeField = new JTextField(5);
        JButton enterButton = makeButton("Enter", 20, 100, 40, fontColor);
        enterButton.addActionListener(e -> {
            String code = gameCodeField.getText();
            Client.connect(GameCode.decode(code));
        });
        codePanel.add(gameCodeField);
        codePanel.add(enterButton);

        JPanel joinPage = new JPanel(new GridBagLayout());
        joinPage.setBackground(new Color(36,34,32));
        JButton backButton = makeButton("<- Back", 34, 300, 55, fontColor);
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MainPage");
        });

        c.gridy = 0;
        c.gridx = 0; 
        c.anchor = GridBagConstraints.CENTER;
        joinPage.add(horsey2, c);
        c.gridy = 1;
        joinPage.add(prompt, c);
        c.gridy = 2;
        joinPage.add(codePanel, c);
        c.gridy = 3;
        joinPage.add(backButton, c);

        mainPanel.add(mainpage, "MainPage");
        mainPanel.add(joinPage, "JoinGame");
        mainPanel.add(hostPage, "HostGame");


        add(mainPanel);

        CardLayout cl = (CardLayout)(mainPanel.getLayout());
        cl.show(mainPanel, "MainPage");

        setVisible(true);
    }

    public static JButton makeButton(String name, int size, int width, int height, Color fontColor)
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
