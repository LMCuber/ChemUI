import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;

public class OptionPanel extends JPanel {
    public OptionPanel(int x, int y, int w, int h, CanvasPanel canvasPanel) {
        // layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
//        gbc.fill = GridBagConstraints.BOTH;
        GridBagLayout layout = new GridBagLayout();
        // option panel
        Font titleFont = new Font("Courier New",Font.BOLD, 40);
        Font font = new Font("Courier New", Font.ITALIC, 20);
        setBounds(x, y, w, h);
        setBackground(new Color(140, 140, 140));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(layout);

        // title
        JLabel titleLabel = new JLabel("Options");
        titleLabel.setFont(titleFont);

        // chemical text input
        JTextField chemNameField = new JTextField();
        chemNameField.setPreferredSize(new Dimension(180, 40));
        chemNameField.setFont(font);
        chemNameField.setToolTipText("Enter chemical name you want to display in 3D");
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chemName = e.getActionCommand();
                String stdinchi = HttpManager.get(HttpManager.opsin + chemName + ".cml");
                try {
                    canvasPanel.processCml(stdinchi);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        chemNameField.setAction(enterAction);

        // placing stuff with GridBag
        gbc.gridy = 0;
        this.add(titleLabel, gbc);
        gbc.gridy = 1;
        this.add(chemNameField, gbc);
    }
}
