import java.util.List;

public class SelectableDecorator implements Selectable {
    protected final Selectable selectable;

    public SelectableDecorator(Selectable selectable) {
        this.selectable = selectable;
    }

    @Override
    public void draw(List<Item> items) {
        selectable.draw(items);
    }

    @Override
    public void addItem(Item item) {
        selectable.addItem(item);
    }
}
