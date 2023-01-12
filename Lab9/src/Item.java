import org.opencv.core.Mat;

public abstract class Item {
    private MyPoint position;
    private boolean isSelected = false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return String.format("%-15s%-10s%s", getClass().getName(), "position:", position);
    }
}
