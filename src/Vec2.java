public class Vec2 {
    public double x;
    public double y;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public Vec2 mult(Vec2 other) {
        return new Vec2(this.x * other.x, this.y * other.y);
    }

    public Vec2 div(Vec2 other) {
        return new Vec2(this.x / other.x, this.y / other.y);
    }

    public double dot(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vec2 cross(Vec2 other) {
        return new Vec2(this.x * other.y + this.x * other.x, this.y * other.y + this.y * other.x);
    }

    public double length() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    @Override
    public String toString() {
        return String.format("Vec2(%f, %f)", this.x, this.y);
    }
}
