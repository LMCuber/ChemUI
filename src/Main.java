import java.time.Instant;


public class Main {
    static long now = Instant.now().toEpochMilli();
    public static void main(String[] args) {
        Game game = new Game(1000, 800);
    }
}