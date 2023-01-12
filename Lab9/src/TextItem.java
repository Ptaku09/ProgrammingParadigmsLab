import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class TextItem extends Item {
    private static final int FONT_SIZE = 23;
    private static final int FONT_WIDTH = 15;
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
        points[1] = new MyPoint(getPosition().getX() + text.length() * FONT_WIDTH, getPosition().getY());
        points[2] = new MyPoint(getPosition().getX() + text.length() * FONT_WIDTH, getPosition().getY() + FONT_SIZE);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + FONT_SIZE);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        MyPoint position = getPosition();
        // adjust position to match the upper left corner of the text
        position.add(new MyPoint(0, FONT_SIZE));

        Imgproc.putText(image, text, position.toPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);

        return image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
