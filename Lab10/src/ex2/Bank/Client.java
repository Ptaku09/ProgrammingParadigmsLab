package ex2.Bank;

import java.util.Random;

public class Client implements Runnable {
    private enum Operation {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    private static final float MIN_AMOUNT = 200f;
    private static final float MAX_AMOUNT = 10000f;
    private static final Random RAND = new Random();
    private final Account account;

    public Client(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
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
