import org.opencv.core.Mat;

import java.util.ArrayList;

public class ComplexItem extends Item {
    private final ArrayList<Item> children;

    public ComplexItem(MyPoint position) {
        super(position);

        this.children = new ArrayList<>();
    }

    @Override
    public void translate(MyPoint p) {
        MyPoint position = getPosition();
        position.add(p);

        setPosition(position);

        for (Item child : children) {
            child.translate(p);
        }
    }

    @Override
    public MyPoint[] getBoundingBox() {
        MyPoint[] points = new MyPoint[4];

        int maxX = 0;
        int maxY = 0;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (Item item : children) {
            MyPoint[] itemPoints = item.getBoundingBox();

            maxX = Math.max(maxX, itemPoints[2].getX());
            maxY = Math.max(maxY, itemPoints[2].getY());
            minX = Math.min(minX, itemPoints[0].getX());
            minY = Math.min(minY, itemPoints[0].getY());
        }

        points[0] = new MyPoint(minX, minY);
        points[1] = new MyPoint(maxX, minY);
        points[2] = new MyPoint(maxX, maxY);
        points[3] = new MyPoint(minX, maxY);

        setPosition(points[0]);

        return points;
    }

    @Override
    public Mat draw(Mat image) {
        for (Item child : children) {
            image = child.draw(image);
        }

        return image;
    }

    public void addChild(Item child) {
        child.translate(getPosition());
        children.add(child);
    }

    public ArrayList<Item> getChildren() {
        return children;
    }
}
