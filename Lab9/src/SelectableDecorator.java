public class SelectableDecorator implements Selectable {
    private final Selectable selectable;

    public SelectableDecorator(Selectable selectable) {
        this.selectable = selectable;
    }

    @Override
    public void draw() {
        selectable.draw();
    }

    @Override
    public void addItem(Item item) {
        selectable.addItem(item);
    }
}
