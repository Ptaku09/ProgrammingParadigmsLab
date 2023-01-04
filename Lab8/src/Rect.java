import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Rect extends Shape {
    private int width;
    private int height;

    public Rect(MyPoint position, int width, int height, boolean isFilled) {
        super(position, isFilled);

        this.width = width;
        this.height = height;
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
        points[1] = new MyPoint(getPosition().getX() + width, getPosition().getY());
        points[2] = new MyPoint(getPosition().getX() + width, getPosition().getY() + height);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + height);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        MyPoint[] points = getBoundingBox();

        if (isFilled())
            Imgproc.rectangle(image, points[0].toPoint(), points[2].toPoint(), new Scalar(0, 0, 255), -1);
        else
            Imgproc.rectangle(image, points[0].toPoint(), points[2].toPoint(), new Scalar(0, 0, 255), 2);

        return image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
