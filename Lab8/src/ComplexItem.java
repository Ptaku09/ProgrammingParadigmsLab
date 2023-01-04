import org.opencv.core.Mat;

import java.util.ArrayList;

public class ComplexItem extends Item {
    private ArrayList<Item> children;

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
        return new MyPoint[0];
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
