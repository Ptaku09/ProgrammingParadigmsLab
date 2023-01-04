import org.opencv.core.Mat;

public abstract class Item {
    private MyPoint position;

    public Item(MyPoint position) {
        this.position = position;
    }

    public abstract void translate(MyPoint p);

    public abstract MyPoint[] getBoundingBox();

    public abstract Mat draw(Mat image);

    public MyPoint getPosition() {
        return position;
    }

    public void setPosition(MyPoint position) {
        this.position = position;
    }
}
