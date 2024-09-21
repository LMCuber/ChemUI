import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CanvasPanel extends JPanel implements Runnable {
    // game loop
    Thread gameThread;
    // 3D calculations
    double GIGA = Math.pow(10, 9);
    double MEGA = Math.pow(10, 6);
    ArrayList<Matrix> vertices = new ArrayList<>();
    ArrayList<int[]> twodVertices = new ArrayList<>();
    int[][] connections;
    double xa, ya, za;
    double xav = 0.005;
    double yav = 0.005;
    double zav = 0.005;
    int r = 30;
    int xo, yo;
    int m = 100;

    Matrix m1 = Matrix.rotationZ(Math.PI / 2);
    Matrix m2 = new Matrix(
        new double[][]{
            {1},
            {0},
            {0},
        });

    public CanvasPanel(int x, int y, int w, int h) {
        setBounds(x, y, w, h);
        xo = w / 2;
        yo = h / 2;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(new Color(50, 50, 50));
        initialize3D();
        startGameLoop();
    }

    private void initialize3D() {
        // vertices
        vertices.add(new Matrix(new double[][]{{-1.0}, {-1.0}, {-1.0}}));
        vertices.add(new Matrix(new double[][]{{1.0}, {-1.0}, {-1.0}}));
        vertices.add(new Matrix(new double[][]{{1.0}, {1.0}, {-1.0}}));
        vertices.add(new Matrix(new double[][]{{-1.0}, {1.0}, {-1.0}}));
        vertices.add(new Matrix(new double[][]{{-1.0}, {-1.0}, {1.0}}));
        vertices.add(new Matrix(new double[][]{{1.0}, {-1.0}, {1.0}}));
        vertices.add(new Matrix(new double[][]{{1.0}, {1.0}, {1.0}}));
        vertices.add(new Matrix(new double[][]{{-1.0}, {1.0}, {1.0}}));
        // connections
        connections = new int[][]{
            {0, 1},  // front
            {1, 2},
            {2, 3},
            {3, 0},
            {4, 5},  // back
            {5, 6},
            {6, 7},
            {7, 4},
            {0, 4},  // inter
            {1, 5},
            {2, 6},
            {3, 7},
        };
    }

    public void startGameLoop() {
        // new game thread boiling
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateGame() {
        xa += xav;
        ya += yav;
        za += zav;
        Matrix rotatedVertex, projectedVertex;
        int posX, posY;
        // update the position of the matrices by using [ortho * rotZ * (rotY * ((rotX * vertex)))]
        twodVertices.clear();
        for (Matrix vertex : vertices) {
            rotatedVertex = Matrix.rotationX(xa).mult(vertex);
            rotatedVertex = Matrix.rotationY(ya).mult(rotatedVertex);
            rotatedVertex = Matrix.rotationZ(za).mult(rotatedVertex);
            projectedVertex = Matrix.projectionOrtho.mult(rotatedVertex);
            posX = (int) (projectedVertex.get(0, 0) * m + xo);
            posY = (int) (projectedVertex.get(1, 0) * m + yo);
            twodVertices.add(new int[]{posX, posY});
        }
    }

    public void paintComponent(Graphics g) {
        /* UPDATE */
        updateGame();

        /* DRAW */
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.PINK);
        g2.fillRect(xo, yo, 40, 80);
        g2.setColor(Color.GREEN);

        // render the points
        int blitX, blitY;
        int i = 0;
        for (int[] vertex : twodVertices) {
            i++;
            blitX = vertex[0] - r / 2;
            blitY = vertex[1] - r / 2;
            g2.fillOval(blitX, blitY, r, r);
        }
        // render the connections
        int startX, startY, endX, endY;
        for (int[] conn : connections) {
            startX = twodVertices.get(conn[0])[0];
            startY = twodVertices.get(conn[0])[1];
            endX = twodVertices.get(conn[1])[0];
            endY = twodVertices.get(conn[1])[1];
            g2.setColor(new Color(240, 240, 240));
            g2.drawLine(startX, startY, endX, endY);
        }

        g2.dispose();
    }

    @Override
    public void run() {
        // main loop specific variables
        int targetFPS = 120;
        double targetSPF = 1 / (double) targetFPS;
        double targetNPF = targetSPF * Math.pow(10, 9);
        double lastTick = System.nanoTime();
        double now, dt, fps;
        double lastFrame = System.nanoTime();
        int frames = 0;
        // loop
        while (gameThread != null) {
            // now only
            now = System.nanoTime();
            if (now - lastTick >= targetNPF) {
                /* MUST BE HERE UNDER THIS IF STATEMENT [IF IT NEEDS TO BE UPDATED EVERY FRAME] */
                // frame stuff
                dt = (now - lastTick) * Math.pow(10, -9); // in seconds
                fps = 1 / dt;

                // actual updating the games
                repaint();

                // final stuff
                lastTick = now;
            }
        }
    }

}
