import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SelectionDecorator extends SelectableDecorator {
    public SelectionDecorator(Selectable selectable) {
        super(selectable);
    }

    @Override
    public void draw(List<Item> items) {
        System.out.println("Select items:");
        selectItems(items);

        super.draw(items);
    }

    private void selectItems(List<Item> items) {
        List<Integer> selectedItems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int input = 1;

        displayItems(items, selectedItems);

        while (input != 0) {
            try {
                input = scanner.nextInt();
                System.out.println();

                if (input > 0 && input <= items.size()) {
                    selectedItems.add(input);
                    items.get(input - 1).setSelected(true);

                    displayItems(items, selectedItems);
                } else if (input != 0) {
                    System.out.println("Item does not exist.");
                    System.out.print("Select item: ");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input");
                System.out.print("Select item: ");
            }

            scanner.nextLine();
        }
    }

    private void displayItems(List<Item> items, List<Integer> selectedItems) {
        for (int i = 0; i < items.size(); i++)
            if (selectedItems.contains(i + 1))
                System.out.printf("%-4s%s\n", "X", items.get(i));
            else
                System.out.printf("%-4d%s\n", i + 1, items.get(i));

        System.out.printf("%-4s%s\n", "0", "Draw");
        System.out.print("\nIndex to select: ");
    }
}
