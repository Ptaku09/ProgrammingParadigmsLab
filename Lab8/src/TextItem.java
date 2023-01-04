import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class TextItem extends Item {
    private String text;

    public TextItem(MyPoint position, String text) {
        super(position);

        this.text = text;
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
        points[1] = new MyPoint(getPosition().getX() + text.length() * 10, getPosition().getY());
        points[2] = new MyPoint(getPosition().getX() + text.length() * 10, getPosition().getY() + 10);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + 10);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        Imgproc.putText(image, text, getPosition().toPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);

        return image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
