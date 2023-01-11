import java.util.List;

public interface SingleTriangle {
    default void removeDuplicatedTriangles(List<Item> items) {
        Item item = null;
        int counter = 0;

        for (Item it : items)
            if (it instanceof Triangle && counter++ < 1)
                item = it;

        if (counter > 1)
            items.remove(item);
    }
}
