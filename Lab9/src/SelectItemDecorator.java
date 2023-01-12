import java.util.Scanner;

public class SelectItemDecorator extends SelectableDecorator {
    public SelectItemDecorator(Selectable selectable) {
        super(selectable);
    }

    @Override
    public void addItem(Item item) {
        System.out.println("Added item:");
        System.out.println(item + "\n");

        Scanner scanner = new Scanner(System.in);
        String answer = null;

        while (!"y".equals(answer) && !"n".equals(answer)) {
            System.out.println("Do you want to select this item? (y/n)");
            answer = scanner.nextLine().toLowerCase();

            if (answer.equals("y")) {
                item.setSelected(true);
            } else if (!answer.equals("n")) {
                System.out.println("Invalid answer\n");
            }
        }

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println();

        super.addItem(item);
    }
}
