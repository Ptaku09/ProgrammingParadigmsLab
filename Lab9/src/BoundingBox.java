import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class BoundingBox extends Item {
    private final MyPoint[] points;

    public BoundingBox(MyPoint position, MyPoint[] points) {
        super(position);
        this.points = points;
    }

    @Override
    public void translate(MyPoint p) {
        for (MyPoint point : points) {
            point.add(p);
        }

        MyPoint position = getPosition();
        position.add(p);

        setPosition(position);
    }

    @Override
    public MyPoint[] getBoundingBox() {
        return points;
    }

    @Override
    public Mat draw(Mat image) {
        Imgproc.rectangle(image, points[0].toPoint(), points[2].toPoint(), new Scalar(0, 0, 0), 1);
        
        return image;
    }
}
