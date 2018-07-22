package fr.awildelephant.bank.account;

import java.time.LocalDate;
import java.util.Objects;

/**
 * An operation on an account.
 */
public final class Operation {

    private final long amount;
    private final long balance;
    private final LocalDate date;

    private Operation(long amount, long balance, LocalDate date) {
        this.amount = amount;
        this.balance = balance;
        this.date = date;
    }

    static Operation operation(long amount, long balance) {
        return new Operation(amount, balance, LocalDate.now());
    }

    static Operation operation(long amount, long balance, LocalDate date) {
        return new Operation(amount, balance, date);
    }

    /**
     * @return the amount of the operation
     */
    public long amount() {
        return amount;
    }

    /**
     * @return the balance of the account after the operation
     */
    public long balance() {
        return balance;
    }

    /**
     * @return the date of the operation
     */
    public LocalDate date() {
        return date;
    }

    @Override
    public String toString() {
        return "Operation[date=" + date + ",amount=" + amount + ",balance=" + balance + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, balance, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operation)) {
            return false;
        }

        final Operation other = (Operation) obj;

        return amount == other.amount
                && balance == other.balance
                && Objects.equals(date, other.date);
    }
}
