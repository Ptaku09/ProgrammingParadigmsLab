package ex1;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger GLOBAL_VARIABLE = new AtomicInteger(0);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        int numberOfThreads = readNumberOfThreads();
        int duration = readDuration();
        ScheduledExecutorService executors = Executors.newScheduledThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executors.scheduleAtFixedRate(() -> {
                int operation = RAND.nextInt(2);

                if (operation == 0)
                    GLOBAL_VARIABLE.incrementAndGet();
                else
                    GLOBAL_VARIABLE.decrementAndGet();
            }, 0, RAND.nextInt(100, 500), TimeUnit.MILLISECONDS);
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));
            executors.shutdown();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        System.out.println("Result: " + GLOBAL_VARIABLE);
    }

    private static int readNumberOfThreads() {
        int n = 0;

        try {
            System.out.print("Enter number of threads: ");
            n = SCANNER.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("You should provide a number!");
        }

        return n;
    }

    private static int readDuration() {
        int n = 0;

        try {
            System.out.print("Enter duration [seconds]: ");
            n = SCANNER.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("You should provide a number!");
        }

        return n;
    }
}