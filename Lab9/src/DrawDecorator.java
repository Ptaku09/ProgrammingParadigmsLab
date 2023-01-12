import java.util.ArrayList;
import java.util.List;

public class DrawDecorator extends SelectableDecorator {
    public DrawDecorator(Selectable selectable) {
        super(selectable);
    }

    @Override
    public void draw(List<Item> items) {
        System.out.println("Drawing...");
        List<Item> boundingBoxes = new ArrayList<>();

        for (Item item : items) {
            if (item.isSelected()) {
                BoundingBox boundingBox = new BoundingBox(item.getPosition(), item.getBoundingBox());
                boundingBoxes.add(boundingBox);
            }
        }

        items.addAll(boundingBoxes);
        super.draw(items);
    }
}
