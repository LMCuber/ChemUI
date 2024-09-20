import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class OptionPanel extends JPanel {
    Font titleFont;
    Font font;
    JLabel titleLabel;
    JLabel distanceLabel;
    JSlider distanceSlider;

    public OptionPanel(int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        // initializing the option panel
        titleFont = new Font("Courier New",Font.BOLD, 30);
        font = new Font("Courier New", Font.ITALIC, 20);
        this.setBounds(0, 0, width, height);
        this.setBackground(new Color(140, 140, 140));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(layout);

        // title
        titleLabel = new JLabel("Options");
        titleLabel.setFont(titleFont);

        // distance
        distanceLabel = new JLabel("Distance");
        distanceLabel.setFont(font);
        distanceSlider = new JSlider();

        // packing stuff
        gbc.gridy = 0;
        this.add(titleLabel, gbc);
        gbc.gridy = 1;
        this.add(distanceLabel, gbc);
        gbc.gridy = 2;
        this.add(distanceSlider, gbc);
    }
}
