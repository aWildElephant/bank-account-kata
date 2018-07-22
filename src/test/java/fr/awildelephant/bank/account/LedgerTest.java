package fr.awildelephant.bank.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static fr.awildelephant.bank.account.Ledger.ledger;
import static fr.awildelephant.bank.account.Operation.operation;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LedgerTest {

    private static final LocalDate LEDGER_CREATION_DATE = LocalDate.of(2018, 1, 1);

    private Ledger ledger;

    @BeforeEach
    void setUp() {
        ledger = ledger(LEDGER_CREATION_DATE);
    }

    @Test
    void it_should_start_with_a_balance_of_zero() {
        assertThat(ledger.balance()).isEqualTo(0);
    }

    @Test
    void the_balance_should_change_after_adding_an_entry() {
        ledger.addEntry(100_00, now());

        assertThat(ledger.balance()).isEqualTo(100_00);
    }

    @Test
    void it_should_not_allow_to_add_an_entry_before_the_latest_one() {
        final LocalDate aDate = LocalDate.of(2018, 7, 21);

        ledger.addEntry(10_00, aDate);

        assertThrows(IllegalArgumentException.class, () -> ledger.addEntry(10_00, aDate.minusDays(1)));
    }

    @Test
    void startementFor_produces_a_statement_for_a_given_time_period() {
        final LocalDate firstOperation = LEDGER_CREATION_DATE.plusDays(1);
        final LocalDate secondOperation = LEDGER_CREATION_DATE.plusDays(3);
        final LocalDate thirdOperation = LEDGER_CREATION_DATE.plusDays(5);

        ledger.addEntry(1000_00, LEDGER_CREATION_DATE);
        ledger.addEntry(-10_00, firstOperation);
        ledger.addEntry(-20_00, secondOperation);
        ledger.addEntry(-5_00, thirdOperation);

        final LocalDate statementStart = secondOperation.minusDays(1);

        assertThat(ledger.statementFor(statementStart, Period.ofDays(1)))
                .containsExactly(operation(-20_00, 970_00, secondOperation),
                                 operation(0, 990_00, statementStart));
    }

    @Test
    void statementFor_returns_the_current_balance_if_the_time_period_is_after_the_last_operation() {
        final LocalDate statementStart = LEDGER_CREATION_DATE.plusDays(2);

        ledger.addEntry(1000_00, LEDGER_CREATION_DATE.plusDays(1));

        assertThat(ledger.statementFor(statementStart, Period.ofMonths(1)))
                .containsExactly(operation(0, 1000_00, statementStart));
    }

    @Test
    void statementFor_returns_an_empty_list_if_the_period_is_before_the_account_creation() {
        final LocalDate statementStart = LEDGER_CREATION_DATE.minusDays(2);

        assertThat(ledger.statementFor(statementStart, Period.ofDays(1)))
                .isEmpty();
    }
}