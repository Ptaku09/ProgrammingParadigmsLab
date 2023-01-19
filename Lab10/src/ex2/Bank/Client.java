package ex2.Bank;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final float MIN_AMOUNT = 200f;
    private static final float MAX_AMOUNT = 10000f;
    private static final int MIN_OPERATION_INTERVAL_TIME = 100;
    private static final int MAX_OPERATION_INTERVAL_TIME = 500;
    private static final Random RAND = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Account account;

    private enum Operation {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    public Client(Account account) {
        this.account = account;
    }

    public void start() {
        executor.scheduleAtFixedRate(() -> {
            synchronized (Bank.ACCOUNTS) {
                switch (getRandomOperation()) {
                    case DEPOSIT -> account.deposit(RAND.nextFloat(MIN_AMOUNT, MAX_AMOUNT));
                    case WITHDRAW -> account.withdraw(RAND.nextFloat(MIN_AMOUNT, MAX_AMOUNT));
                    case TRANSFER -> {
                        String accountNumber = Bank.ACCOUNT_NUMBERS.get(RAND.nextInt(Bank.ACCOUNT_NUMBERS.size()));
                        Account accountToTransfer = Bank.ACCOUNTS.get(accountNumber);

                        account.transfer(accountToTransfer, RAND.nextFloat(MIN_AMOUNT, MAX_AMOUNT));
                    }
                }
            }
        }, 0, RAND.nextInt(MIN_OPERATION_INTERVAL_TIME, MAX_OPERATION_INTERVAL_TIME), TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdown();
    }

    private static Operation getRandomOperation() {
        int operation = RAND.nextInt(100);

        if (operation < 5)
            return Operation.DEPOSIT;
        else if (operation < 60)
            return Operation.WITHDRAW;
        else
            return Operation.TRANSFER;
    }
}
