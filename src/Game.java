import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.time.Instant;

public class Game implements Runnable {
    Thread gameThread;
    long lastTick;

    public Game(int windowWidth, int windowHeight){
        // initializing the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(windowWidth, windowHeight);
        frame.getContentPane().setBackground(new Color(90, 90, 90));

        // frame final
        OptionPanel optionPanel = new OptionPanel(200, windowHeight);
        frame.add(optionPanel);
        frame.setVisible(true);

        // new game thread boiling
        gameThread = new Thread(this);
        gameThread.start();
        lastTick = System.nanoTime();
    }

    @Override
    public void run() {
        while (gameThread != null) {
//            System.out.println(i);
        }
    }
}
