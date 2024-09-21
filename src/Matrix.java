import java.util.ArrayList;

public class Matrix {
    private double[][] m;
    private final int rows;
    private final int columns;
    public Matrix(double[][] arr) {
        this.rows = arr.length;
        this.columns = arr[0].length;
        m = arr;
    }

    public static Matrix projectionOrtho = new Matrix(new double[][]{
        {1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0},
        {0.0, 0.0, 0.0},
    });

    public static Matrix rotationX(double a) {
        return new Matrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, Math.cos(a), -Math.sin(a)},
            {0.0, Math.sin(a), Math.cos(a)},
        });
    }

    public static Matrix rotationY(double a) {
        return new Matrix(new double[][]{
            {Math.cos(a), 0.0, Math.sin(a)},
            {0.0, 1.0, 0.0},
            {-Math.sin(a), 0.0, Math.cos(a)},
        });
    }

    public static Matrix rotationZ(double a) {
        return new Matrix(new double[][]{
            {Math.cos(a), -Math.sin(a), 0.0},
            {Math.sin(a), Math.cos(a), 0.0},
            {0.0, 0.0, 1},
        });
    }

    public Matrix mult(Matrix other) {
        Matrix mat = new Matrix(new double[][]{
            {0},
            {0},
            {0},
        });
        for (int r = 0; r < this.columns; r ++) {
            for (int i = 0; i < other.rows; i ++) {
//                System.out.printf("%d %d %f\n", r, i, this.get(r, i) * other.get(i, 0));
                mat.set(r, 0, mat.get(r, 0) + this.get(r, i) * other.get(i, 0));
            }
        }
        return mat;
    }

    public double get(int row, int column) {
        return this.m[row][column];
    }

    public void set(int row, int column, double value) {
        this.m[row][column] = value;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(String.format("Matrix<%d,%d> [\n", this.rows, this.columns));
        for (int y = 0; y < this.rows; y++) {
            ret.append("\t[");
            for (int x = 0; x < this.columns; x++) {
                ret.append(this.m[y][x] + ", ");
            }
            ret.append("]\n");
        }
        ret.append("]");
        return ret.toString();
    }

}
