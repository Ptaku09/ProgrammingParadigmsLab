public class DisplayBoundingBoxDecorator extends SelectableDecorator {
    public DisplayBoundingBoxDecorator(Selectable selectable) {
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

    @Override
    public void draw() {
        System.out.println("Drawing...");
        super.draw();
    }
}
