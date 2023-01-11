import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Spiral extends Shape {
    private int width;
    private int density;

    public Spiral(MyPoint position, int width, int density, boolean isFilled) {
        super(position, isFilled);

        this.width = width;
        this.density = density;
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
        points[2] = new MyPoint(getPosition().getX() + width, getPosition().getY() + width);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + width);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        int width = this.width;
        int stage = 1;
        int shiftMultiplier = 0;
        boolean isShiftNeccessary = true;

        while (width > 0 && density > 0) {
            int currentShift = shiftMultiplier * density;

            for (int i = 0; i < 2; i++) {
                switch (stage) {
                    case 1 -> {
                        Point start = new Point(getPosition().getX() + currentShift, getPosition().getY() + currentShift);
                        Point end = new Point(getPosition().getX() + width + currentShift, getPosition().getY() + currentShift);

                        Imgproc.line(image, start, end, new Scalar(0, 0, 255), 2);
                    }
                    case 2 -> {
                        Point start = new Point(getPosition().getX() + width + currentShift, getPosition().getY() + currentShift);
                        Point end = new Point(getPosition().getX() + width + currentShift, getPosition().getY() + width + currentShift);

                        Imgproc.line(image, start, end, new Scalar(0, 0, 255), 2);
                    }
                    case 3 -> {
                        Point start = new Point(getPosition().getX() + width + currentShift, getPosition().getY() + width + currentShift);
                        Point end = new Point(getPosition().getX() + currentShift, getPosition().getY() + width + currentShift);

                        Imgproc.line(image, start, end, new Scalar(0, 0, 255), 2);
                    }
                    case 4 -> {
                        Point start = new Point(getPosition().getX() + currentShift, getPosition().getY() + width + currentShift);
                        Point end = new Point(getPosition().getX() + currentShift, getPosition().getY() + currentShift);

                        Imgproc.line(image, start, end, new Scalar(0, 0, 255), 2);
                    }
                }

                if (stage == 4)
                    stage = 1;
                else
                    stage++;
            }

            if (isShiftNeccessary) {
                shiftMultiplier++;
                isShiftNeccessary = false;
            } else {
                isShiftNeccessary = true;
            }

            width -= density;
        }

        return image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }
}
