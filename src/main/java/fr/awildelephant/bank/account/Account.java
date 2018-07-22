package fr.awildelephant.bank.account;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static fr.awildelephant.bank.account.Ledger.ledger;
import static java.time.LocalDate.now;

/**
 * Representation of the bank account.
 * <p>
 * This class manages a ledger that holds the history of the account. Its role is to validate the user input before committing it in the <code>Ledger</code>.
 * </p>
 *
 * @see Ledger
 */
public class Account {

    private final Ledger ledger;

    public Account(LocalDate creationDate) {
        ledger = ledger(creationDate);
    }

    /**
     * @param amountInCents the amount to deposit, in cents
     * @throws IllegalArgumentException if the amount is less than or equal to zero
     */
    public synchronized void deposit(long amountInCents) {
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("The amount of money to deposit must be greater than zero");
        }

        ledger.addEntry(amountInCents, now());
    }

    /**
     * @param amountInCents the amount to withdraw, in cents
     * @throws IllegalArgumentException if the amount is less than or equal to zero or greater than the current balance of the account
     */
    public synchronized void withdraw(long amountInCents) {
        if (amountInCents <= 0 || amountInCents > ledger.balance()) {
            throw new IllegalArgumentException("The amount of money to withdraw must be greater than zero");
        }

        ledger.addEntry(-amountInCents, now());
    }

    /**
     * Produce a statement for a period of time
     *
     * @param start  the start date of the period, included
     * @param period the period of time to include after the start date
     * @return the operations on this account during the given period of time, plus an operation describing the balance of the account before that
     * @throws IllegalArgumentException if the period of time is in the future or before the creation of the account
     */
    public synchronized List<Operation> statement(LocalDate start, Period period) {
        if (start.plus(period).isAfter(now())) {
            throw new IllegalArgumentException("Cannot create a statement in the future");
        }

        return ledger.statementFor(start, period);
    }

    /**
     * @return the current balance of the account
     */
    public synchronized long balance() {
        return ledger.balance();
    }
}
