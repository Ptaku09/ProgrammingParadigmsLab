import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Segment extends Primitive {
    private int length;
    private MyPoint start;
    private MyPoint end;

    public Segment(MyPoint position, int length, MyPoint start, MyPoint end) {
        super(position);

        start.add(position);
        end.add(position);

        this.length = length;
        this.start = start;
        this.end = end;
    }

    @Override
    public void translate(MyPoint p) {
        start.add(p);
        end.add(p);

        MyPoint position = getPosition();
        position.add(p);

        setPosition(position);
    }

    @Override
    public MyPoint[] getBoundingBox() {
        MyPoint[] points = new MyPoint[4];

        int maxX = Math.max(start.getX(), end.getX());
        int maxY = Math.max(start.getY(), end.getY());
        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());

        points[0] = new MyPoint(minX, maxY);
        points[1] = new MyPoint(maxX, maxY);
        points[2] = new MyPoint(maxX, minY);
        points[3] = new MyPoint(minX, minY);

        setPosition(points[0]);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        Imgproc.line(image, start.toPoint(), end.toPoint(), new Scalar(0, 0, 255), 2);

        return image;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public MyPoint getStart() {
        return start;
    }

    public void setStart(MyPoint start) {
        this.start = start;
    }

    public MyPoint getEnd() {
        return end;
    }

    public void setEnd(MyPoint end) {
        this.end = end;
    }
}
