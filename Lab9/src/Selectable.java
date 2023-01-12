import java.util.List;

public interface Selectable {
    void draw(List<Item> items);

    void addItem(Item item);
}
