public class DrawDecorator extends SelectableDecorator {
    public DrawDecorator(Selectable selectable) {
        super(selectable);
    }

    @Override
    public void addItem(Item item) {
        if (item.isSelected()) {
            BoundingBox boundingBox = new BoundingBox(item.getPosition(), item.getBoundingBox());
            super.addItem(boundingBox);
        }

        super.addItem(item);
    }
}
