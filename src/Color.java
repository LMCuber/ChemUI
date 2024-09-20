public class Color {
    public static Color WHITE = new Color(20, 20, 30);
    public static Color MUDDY_GRAY = new Color(40, 40, 40, 120);

    public int r;
    public int g;
    public int b;
    public int a;

    public Color(int r, int g, int b) {
        this.defaultColor(r, g, b);
    }

    public Color(int r, int g, int b, int a) {
        this.defaultColor(r, g, b);
        this.a = a;
    }

    private void defaultColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    @Override
    public String toString() {
        return String.format("Color(%d, %d, %d, %d)", this.r, this.g, this.b, this.a);
    }
}
