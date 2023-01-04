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
        points[1] = new MyPoint(getPosition().getX() + 2 * width, getPosition().getY());
        points[2] = new MyPoint(getPosition().getX() + 2 * width, getPosition().getY() + 2 * width);
        points[3] = new MyPoint(getPosition().getX(), getPosition().getY() + 2 * width);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        int width = this.width;
        int iteration = 1;
        int counter = 0;
        boolean isOdd = true;

        while (width > 0) {
            for (int i = 0; i < 2; i++) {
                switch (iteration) {
                    case 1 -> {
                        Imgproc.line(image, new Point(getPosition().getX() + counter * density, getPosition().getY() + counter * density), new Point(getPosition().getX() + width + counter * density, getPosition().getY() + counter * density), new Scalar(0, 0, 255), 2);
                    }
                    case 2 -> {
                        Imgproc.line(image, new Point(getPosition().getX() + width + counter * density, getPosition().getY() + counter * density), new Point(getPosition().getX() + width + counter * density, getPosition().getY() + width + counter * density), new Scalar(0, 0, 255), 2);
                    }
                    case 3 -> {
                        Imgproc.line(image, new Point(getPosition().getX() + width + counter * density, getPosition().getY() + width + counter * density), new Point(getPosition().getX() + counter * density, getPosition().getY() + width + counter * density), new Scalar(0, 0, 255), 2);
                    }
                    case 4 -> {
                        Imgproc.line(image, new Point(getPosition().getX() + counter * density, getPosition().getY() + width + counter * density), new Point(getPosition().getX() + counter * density, getPosition().getY() + counter * density), new Scalar(0, 0, 255), 2);
                    }
                }

                if (iteration == 4) {
                    iteration = 1;
                } else {
                    iteration++;
                }
            }

            width -= density;

            if (isOdd) {
                counter++;
                isOdd = false;
            } else {
                isOdd = true;
            }
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
