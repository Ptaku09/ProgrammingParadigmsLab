package ex2.Bank;

public class Account {
    private final String accountNumber;
    private final float initialBalance;
    private float balance;
    private int deposits = 0;
    private int withdrawals = 0;
    private int transfers = 0;

    public Account(String accountNumber, float balance) {
        this.accountNumber = accountNumber;
        this.initialBalance = balance;
        this.balance = balance;
    }

    public synchronized void deposit(float amount) {
        this.balance += amount;
        this.deposits++;
    }

    public synchronized void transferDeposit(float amount) {
        this.balance += amount;
        this.transfers++;
    }

    public synchronized void withdraw(float amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            this.withdrawals++;
        }
    }

    public synchronized void transfer(Account account, float amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            account.transferDeposit(amount);
            this.transfers++;
        }
    }

    @Override
    public String toString() {
        return String.format("%-20s%-26s\n%-20s%-9.2f\n%-20s%-9.2f\n%-20s%-9d\n%-20s%-9d\n%-20s%-9d",
                "Account number: ", accountNumber, "Initial balance: ", initialBalance, "Current balance: ", balance, "Deposits: ", deposits, "Withdrawals: ", withdrawals, "Transfers: ", transfers);
    }
}
