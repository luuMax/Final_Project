import javax.swing.*;
import java.awt.*;

public class BoardUI extends JFrame{
    
    public void initialize()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setTitle("Chat");
        setSize(800,800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8,8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel();
                if ((i + j) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.GRAY);
                }
                add(square);
            }
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        BoardUI board = new BoardUI();
        board.initialize();
    }

}
