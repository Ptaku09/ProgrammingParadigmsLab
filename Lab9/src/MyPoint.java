import org.opencv.core.Point;

public class MyPoint {
    private int x;
    private int y;

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(MyPoint p) {
        x += p.x;
        y += p.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point toPoint() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return String.format("%-1s%-4d%-2s%-4d%-1s", "(", x, ", ", y, ")");
    }
}
