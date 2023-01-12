public class Main {
    public static void main(String[] args) {
        Rect rect = new Rect(new MyPoint(0, 0), 100, 150, true);
        Triangle triangle = new Triangle(new MyPoint(0, 0), new MyPoint(0, 400), new MyPoint(100, 100), new MyPoint(0, 0), true);
        Circle circle = new Circle(new MyPoint(500, 500), 250, true);
        Segment segment = new Segment(new MyPoint(0, 0), 200, new MyPoint(0, 0), new MyPoint(1920, 1080));
        TextItem textItem = new TextItem(new MyPoint(200, 300), "Hello World!");
        Spiral spiral = new Spiral(new MyPoint(1000, 200), 200, 5, false);

        triangle.translate(new MyPoint(300, 400));

        // Snowman ---------------------------------------------------------------------------------------------
        Circle bottom = new Circle(new MyPoint(0, 0), 100, false);
        Circle middle = new Circle(new MyPoint(30, -140), 70, false);
        ComplexItem top = new ComplexItem(new MyPoint(50, -240));

        Circle head = new Circle(new MyPoint(0, 0), 50, false);
        Circle eye1 = new Circle(new MyPoint(20, 20), 10, true);
        Circle eye2 = new Circle(new MyPoint(60, 20), 10, true);
        Triangle nose = new Triangle(new MyPoint(40, 50), new MyPoint(0, 0), new MyPoint(20, 0), new MyPoint(50, 60), true);
        Segment mouth = new Segment(new MyPoint(0, 80), 50, new MyPoint(40, 0), new MyPoint(60, 0));

        top.addChild(head);
        top.addChild(eye1);
        top.addChild(eye2);
        top.addChild(nose);
        top.addChild(mouth);

        ComplexItem snowman = new ComplexItem(new MyPoint(1300, 300));
        snowman.addChild(bottom);
        snowman.addChild(middle);
        snowman.addChild(top);
        // -----------------------------------------------------------------------------------------------------

        SelectableDecorator selectableScene = new SelectionDecorator(new DrawDecorator(new Scene()));
        Scene scene = new Scene();
        selectableScene.addItem(rect);
        selectableScene.addItem(triangle);
        selectableScene.addItem(circle);
        selectableScene.addItem(segment);
        selectableScene.addItem(textItem);
        selectableScene.addItem(snowman);
        selectableScene.addItem(spiral);

        scene.addItem(rect);
        scene.addItem(triangle);
        scene.addItem(circle);
        scene.addItem(segment);
        scene.addItem(textItem);
        scene.addItem(snowman);
        scene.addItem(spiral);

        selectableScene.draw(scene.getItems());
    }
}
