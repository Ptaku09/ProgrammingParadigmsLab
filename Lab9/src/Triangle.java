import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class Triangle extends Shape {
    private MyPoint p1;
    private MyPoint p2;
    private MyPoint p3;

    public Triangle(MyPoint position, MyPoint p1, MyPoint p2, MyPoint p3, boolean isFilled) {
        super(position, isFilled);

        p1.add(position);
        p2.add(position);
        p3.add(position);

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @Override
    public void translate(MyPoint p) {
        p1.add(p);
        p2.add(p);
        p3.add(p);

        MyPoint position = getPosition();
        position.add(p);

        setPosition(position);
    }

    @Override
    public MyPoint[] getBoundingBox() {
        MyPoint[] points = new MyPoint[4];

        int maxX = Math.max(p1.getX(), Math.max(p2.getX(), p3.getX()));
        int maxY = Math.max(p1.getY(), Math.max(p2.getY(), p3.getY()));
        int minX = Math.min(p1.getX(), Math.min(p2.getX(), p3.getX()));
        int minY = Math.min(p1.getY(), Math.min(p2.getY(), p3.getY()));

        points[0] = new MyPoint(minX, minY);
        points[1] = new MyPoint(maxX, minY);
        points[2] = new MyPoint(maxX, maxY);
        points[3] = new MyPoint(minX, maxY);

        setPosition(points[0]);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        List<MatOfPoint> list = List.of(new MatOfPoint(p1.toPoint(), p2.toPoint(), p3.toPoint()));

        if (isFilled())
            Imgproc.fillPoly(image, list, new Scalar(0, 0, 255));
        else
            Imgproc.drawContours(image, list, 0, new Scalar(0, 0, 255), 2);

        return image;
    }

    public MyPoint getP1() {
        return p1;
    }

    public void setP1(MyPoint p1) {
        this.p1 = p1;
    }

    public MyPoint getP2() {
        return p2;
    }

    public void setP2(MyPoint p2) {
        this.p2 = p2;
    }

    public MyPoint getP3() {
        return p3;
    }

    public void setP3(MyPoint p3) {
        this.p3 = p3;
    }
}
