import org.w3c.dom.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

public class CanvasPanel extends JPanel implements Runnable, MouseWheelListener, MouseListener, MouseMotionListener {
    // implementations
    Thread gameThread;
    // 3D calculations
    ArrayList<Matrix> vertices = new ArrayList<>();
    ArrayList<int[]> twodVertices = new ArrayList<>();
    ArrayList<int[]> connections = new ArrayList<>();
    ArrayList<Integer> bonds = new ArrayList<>();
    ArrayList<Color> points = new ArrayList<>();
    ArrayList<String> atomTypes = new ArrayList<>();
    double xa, ya, za, xav, yav, zav;
    int xo, yo;
    int m = 70;
    int lastMouseX;
    int lastMouseY;

    Matrix m1 = Matrix.rotationZ(Math.PI / 2);
    Matrix m2 = new Matrix(
        new double[][]{
            {1},
            {0},
            {0},
        });
    AtomColor atomColor = new AtomColor();

    public CanvasPanel(int x, int y, int w, int h) {
        // implementations
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        // frame
        setBounds(x, y, w, h);
        xo = w / 2;
        yo = h / 2;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(new Color(50, 50, 50));
        initialize3DSquare();
        startGameLoop();
    }

    private int getR() {
        return (int) (m * 0.7);
    }

    private Font getAtomFont() {
        return new Font("Courier New", Font.PLAIN, (int) (getR() * 0.7));
    }

    private BasicStroke getBondWidth(int index) {
        return new BasicStroke((float) (getR() * 0.1 * Math.pow(bonds.get(index), 1.6)));
    }

    private void initialize3DSquare() {
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
        connections.add(new int[]{0, 1});
        connections.add(new int[]{1, 2});
        connections.add(new int[]{2, 3});
        connections.add(new int[]{3, 0});
        connections.add(new int[]{4, 5});
        connections.add(new int[]{5, 6});
        connections.add(new int[]{6, 7});
        connections.add(new int[]{7, 4});
        connections.add(new int[]{0, 4});
        connections.add(new int[]{1, 5});
        connections.add(new int[]{2, 6});
        connections.add(new int[]{3, 7});
        for (int i = 0; i < connections.toArray().length; i++) {
            bonds.add(1);
        }
        // points
        for (int i = 0; i < vertices.toArray().length; i++) {
            points.add(Color.CYAN);
        }
        // velocities
        xa = ya = za = 0;
        xav = yav = zav = 0;
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
        Point p = MouseInfo.getPointerInfo().getLocation();
        lastMouseX = (int) p.getX();
        lastMouseY = (int) p.getY();
    }

    public void paintComponent(Graphics g) {
        /* UPDATE */
        updateGame();

        /* DRAW */
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // render the points
        int blitX, blitY;
        // render the connections
        int startX, startY, endX, endY;
        g2.setColor(new Color(150, 150, 150));
        int i = 0;
        for (int[] conn : connections) {
            startX = twodVertices.get(conn[0])[0];
            startY = twodVertices.get(conn[0])[1];
            endX = twodVertices.get(conn[1])[0];
            endY = twodVertices.get(conn[1])[1];
            g2.setStroke(getBondWidth(i));
            g2.drawLine(startX, startY, endX, endY);
            i++;
        }
        // render the points
        i = 0;
        Color pointColor;
        for (int[] vertex : twodVertices) {
            blitX = vertex[0] - getR() / 2;
            blitY = vertex[1] - getR() / 2;
            pointColor = points.get(i);
            g2.setColor(pointColor);
            g2.fillOval(blitX, blitY, getR(), getR());
            if (!atomTypes.isEmpty()) {
                g2.setColor(atomColor.oppos.get(atomTypes.get(i)));
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(getAtomFont());
                g2.drawString(atomTypes.get(i), (int) (blitX - getR() * 0.2 + (double) getR() / 2), (int) (blitY + getR() * 0.25 + (double) getR() / 2));
            }
            i++;
        }

        g2.dispose();
    }

    public void processCml(String cml) throws Exception {
        // vertices
        vertices.clear();
        connections.clear();
        bonds.clear();
        points.clear();
        atomTypes.clear();
        // process the xml and append to the vertices arraylist
        Document doc = HttpManager.loadXMLFromString(cml);
        // parse the atoms
        NodeList atomNodeList = doc.getElementsByTagName("atom");
        for (int i = 0; i < atomNodeList.getLength(); i++) {
            // parse xml
            Node node = atomNodeList.item(i);
            Element elem = (Element) node;
            // get attributes like position, atom type etc.
            String atomType = elem.getAttribute("elementType");
            Color color = atomColor.color.get(atomType);
            double xPos = Double.parseDouble(elem.getAttribute("x2"));
            double yPos = Double.parseDouble(elem.getAttribute("y2"));
            // create and add new (vertex, point, atom-type) data
            vertices.add(new Matrix(new double[][]{{xPos}, {yPos}, {0.0}}));
            points.add(color);
            atomTypes.add(atomType);
        }
        // process the bonds between atoms
        NodeList bondNodeList = doc.getElementsByTagName("bond");
        for (int i = 0; i < bondNodeList.getLength(); i++) {
            // parse xml
            Node node = bondNodeList.item(i);
            Element elem = (Element) node;
            // get attributes like the source and target bond (commutative but whatever idc)
            String bondRef = elem.getAttribute("atomRefs2");
            int atomIndex1 = Integer.parseInt(bondRef.split(" ")[0].replaceFirst("^a", "")) - 1;
            int atomIndex2 = Integer.parseInt(bondRef.split(" ")[1].replaceFirst("^a", "")) - 1;
            String bondType = elem.getAttribute("order");
            switch (bondType) {
                case "S" -> bonds.add(1);
                case "D" -> bonds.add(2);
                case "T" -> bonds.add(3);
            }
            // create and add new bond data
            connections.add(new int[]{atomIndex1, atomIndex2});
        }
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        m += e.getWheelRotation() * -8;
        m = Math.max(Math.min(m, 300), 18);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        float mult = 0.001f;
        int dx = (int) (lastMouseX - MouseInfo.getPointerInfo().getLocation().getX());
        int dy = (int) (lastMouseY - MouseInfo.getPointerInfo().getLocation().getY());
        ya += dx * mult;
        xa -= dy * mult;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
