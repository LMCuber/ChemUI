import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;

public class Game {
    Thread gameThread;
    double lastTick;
    ArrayList<Matrix> vertices;
    OptionPanel optionPanel;
    CanvasPanel canvasPanel;

    public Game(int windowWidth, int windowHeight){
        // initializing the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(windowWidth, windowHeight);
        frame.getContentPane().setBackground(new Color(90, 90, 90));

        // frame final with all the panels
        int optionPanelWidth = 200;
        optionPanel = new OptionPanel(0, 0, optionPanelWidth, windowHeight);
        canvasPanel = new CanvasPanel(optionPanelWidth, 0, windowWidth - optionPanelWidth, windowHeight);

        frame.add(optionPanel);
        frame.add(canvasPanel);
        frame.setVisible(true);
    }
}
