package ex2.Bank;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Bank {
    protected static final List<String> ACCOUNT_NUMBERS = new ArrayList<>();
    protected static final Map<String, Account> ACCOUNTS = new HashMap<>();
    private static final Random RAND = new Random();
    private final int numberOfClients;

    public Bank(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void initSimulation() {
        List<Client> threads = new ArrayList<>();
        int duration = readDuration();
        generateAccounts();

        for (String accountNumber : ACCOUNT_NUMBERS) {
            Client th = new Client(ACCOUNTS.get(accountNumber));
            th.start();
            threads.add(th);
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(duration));

            for (Client th : threads)
                th.stop();
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
