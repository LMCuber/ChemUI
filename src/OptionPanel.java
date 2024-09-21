import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class OptionPanel extends JPanel {
    Font titleFont;
    Font font;
    JLabel titleLabel;
    JLabel distanceLabel;
    JSlider distanceSlider;
    JLabel distanceValue;

    public OptionPanel(int x, int y, int w, int h) {
        // layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
//        gbc.fill = GridBagConstraints.BOTH;
        GridBagLayout layout = new GridBagLayout();
        // option panel
        titleFont = new Font("Courier New",Font.BOLD, 30);
        font = new Font("Courier New", Font.ITALIC, 20);
        setBounds(x, y, w, h);
        setBackground(new Color(140, 140, 140));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(layout);

        // title
        titleLabel = new JLabel("Options");
        titleLabel.setFont(titleFont);

        // distance
        distanceSlider = new JSlider();
        distanceLabel = new JLabel("Distance");
        distanceLabel.setFont(font);
        distanceValue = new JLabel("1");

        // placing stuff with GridBag
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.gridheight = 1;
        this.add(titleLabel, gbc);
        gbc.gridy = 1;
        this.add(distanceLabel, gbc);
        gbc.gridy = 2; gbc.gridwidth = 1;
        this.add(distanceSlider, gbc);
        gbc.gridx = 1;
        this.add(distanceValue, gbc);
    }
}
