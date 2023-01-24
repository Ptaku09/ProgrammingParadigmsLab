package ex2.Bank;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bank {
    private static final int MIN_OPERATION_INTERVAL_TIME = 100;
    private static final int MAX_OPERATION_INTERVAL_TIME = 500;
    protected static final List<String> ACCOUNT_NUMBERS = new ArrayList<>();
    protected static final Map<String, Account> ACCOUNTS = new HashMap<>();
    private static final Random RAND = new Random();
    private final int numberOfClients;
    private final ScheduledExecutorService executors;

    public Bank(int numberOfClients) {
        this.numberOfClients = numberOfClients;
        executors = Executors.newScheduledThreadPool(numberOfClients);
    }

    public void initSimulation() {
        int duration = readDuration();
        generateAccounts();

        for (String accountNumber : ACCOUNT_NUMBERS) {
            executors.scheduleAtFixedRate(new Client(ACCOUNTS.get(accountNumber)), 0, RAND.nextInt(MIN_OPERATION_INTERVAL_TIME, MAX_OPERATION_INTERVAL_TIME), TimeUnit.MILLISECONDS);
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));
            executors.shutdown();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        printAccounts();
    }

    private int readDuration() {
        Scanner scanner = new Scanner(System.in);
        int n = 0;

        try {
            System.out.print("Enter duration [seconds]: ");
            n = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("You should provide a number!");
        }

        return n;
    }

    private void generateAccounts() {
        for (int i = 0; i < numberOfClients; i++) {
            addAccount(generateAccountNumber(), generateBalance());
        }
    }

    private String generateAccountNumber() {
        return RandomStringUtils.randomNumeric(26);
    }

    private float generateBalance() {
        return RAND.nextFloat(5000, 20000);
    }

    private void addAccount(String accountNumber, float balance) {
        if (ACCOUNT_NUMBERS.contains(accountNumber)) {
            System.out.println("Account number already exists!");
            return;
        }

        ACCOUNT_NUMBERS.add(accountNumber);
        ACCOUNTS.put(accountNumber, new Account(accountNumber, balance));
    }

    private void printAccounts() {
        for (Account account : ACCOUNTS.values()) {
            System.out.println(account);
            System.out.println("\n--------------------------------------------------\n");
        }
    }
}
