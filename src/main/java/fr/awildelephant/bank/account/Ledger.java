package fr.awildelephant.bank.account;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

import static fr.awildelephant.bank.account.Operation.operation;

/**
 * Represents a ledger.
 */
class Ledger {

    private final Stack<Operation> stack;

    private Ledger(LocalDate creationDate) {
        stack = new Stack<>();

        stack.push(initialBalance(creationDate, 0));
    }

    static Ledger ledger(LocalDate creationDate) {
        return new Ledger(creationDate);
    }

    /**
     * Add an entry in the ledger.
     *
     * @param amount the delta of the entry
     * @param date the date of the entry
     * @throws IllegalArgumentException if the date is before the latest entry of the ledger
     */
    void addEntry(long amount, LocalDate date) {
        if (date.isBefore(stack.peek().date())) {
            throw new IllegalArgumentException();
        }

        final long previousBalance = balance();

        stack.push(operation(amount, previousBalance + amount, date));
    }

    /**
     * @return the balance after the latest entry of the ledger
     */
    long balance() {
        return stack.peek().balance();
    }

    /**
     * Produces a statement for the given time period.
     *
     * @param start the start of the period
     * @param period the duration of the period
     * @return the list of operation during this period, by date descending. The initial balance of the account is added in the last operation with a nil amount. If the period is before the creation of the account, returns an empty list
     */
    List<Operation> statementFor(LocalDate start, Period period) {
        final LocalDate end = start.plus(period);

        if (start.isAfter(stack.peek().date())) {
            return List.of(initialBalance(start, balance()));
        }

        if (end.isBefore(stack.get(0).date())) {
            return List.of();
        }

        final ArrayList<Operation> result = new ArrayList<>();

        final Predicate<Operation> isAfterPeriodStart = isAfter(start);
        final Predicate<Operation> isBeforePeriodEnd = isBefore(end);

        int i = stack.size() - 1;
        while (i > 0) {
            final Operation operation = stack.get(i);

            if (isAfterPeriodStart.test(operation)) {
                if (isBeforePeriodEnd.test(operation)) {
                    result.add(operation);
                }
            } else {
                break;
            }
            i--;
        }

        result.add(initialBalance(start, stack.get(i).balance()));

        return result;
    }

    private static Operation initialBalance(LocalDate date, long balance) {
        return operation(0, balance, date);
    }

    private static Predicate<Operation> isBefore(LocalDate reference) {
        return operation -> {
            final LocalDate date = operation.date();

            return date.isBefore(reference) || date.isEqual(reference);
        };
    }

    private static Predicate<Operation> isAfter(LocalDate reference) {
        return operation -> {
            final LocalDate date = operation.date();

            return date.isAfter(reference) || date.isEqual(reference);
        };
    }
}
