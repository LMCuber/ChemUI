import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class AtomColor {
    public Map<String, Color> color = new HashMap<>();
    public Map<String, Color> oppos = new HashMap<>();
    public Map<String, Double> radii = new HashMap<>();

    public AtomColor() {
        // opposite of atom colors
        color.put("C", new Color(30, 30, 30));
        color.put("H", new Color(220, 220, 220));
        color.put("O", new Color(180, 0, 0));
        color.put("N", new Color(135, 206, 250));
        // atom colors
        oppos.put("C", new Color(255, 255, 255));
        oppos.put("H", new Color(0, 0, 0));
        oppos.put("O", new Color(255, 255, 255));
        oppos.put("N", new Color(255, 255, 255));
        // atomic radii (relative to r, so they are a multiplier)
        // TODO: yay or nay??
        // FIXME: piss green is a cool color isn't it
    }
}
