import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Circle extends Shape {
    private int radius;

    public Circle(MyPoint position, int radius, boolean isFilled) {
        super(position, isFilled);

        this.radius = radius;
    }

    @Override
    public void translate(MyPoint p) {
        MyPoint position = getPosition();
        position.add(p);

        setPosition(position);
    }

    @Override
    public MyPoint[] getBoundingBox() {
        MyPoint[] points = new MyPoint[4];

        points[0] = getPosition();
        points[1] = new MyPoint(getPosition().getX() + 2 * radius, getPosition().getY());
        points[2] = new MyPoint(getPosition().getX() + 2 * radius, getPosition().getY() + 2 * radius);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + 2 * radius);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        MyPoint[] points = getBoundingBox();

        int centerX = (points[0].getX() + points[1].getX()) / 2;
        int centerY = (points[0].getY() + points[3].getY()) / 2;

        if (isFilled())
            Imgproc.circle(image, new Point(centerX, centerY), radius, new Scalar(50, 50, 15), -1);
        else
            Imgproc.circle(image, new Point(centerX, centerY), radius, new Scalar(0, 0, 255), 2);

        return image;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
