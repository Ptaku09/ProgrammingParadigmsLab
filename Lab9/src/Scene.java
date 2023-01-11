import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

public class Scene implements SingleTriangle {
    private final List<Item> items;

    public Scene() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        removeDuplicatedTriangles(items);
    }

    public void draw() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = new Mat(1080, 1920, CvType.CV_8UC3, new Scalar(255, 255, 255));

        for (Item item : items) {
            item.draw(mat);
        }

        Imgcodecs.imwrite("image.jpg", mat);
//        HighGui.imshow("Your draw", mat);
//        HighGui.waitKey();
    }
}
