import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;

    public GameFrame(int size) throws HeadlessException {
        setBounds(100, 100, size, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}
