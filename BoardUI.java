import javax.swing.*;
import java.awt.*;

public class BoardUI extends JFrame{
    
    public void initialize()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setTitle("Chat");
        setSize(600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {
        BoardUI bo = new BoardUI();
        bo.initialize();
    }

}
