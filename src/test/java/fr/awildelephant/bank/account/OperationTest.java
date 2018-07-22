package fr.awildelephant.bank.account;

import fr.awildelephant.bank.account.Operation;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static fr.awildelephant.bank.account.Operation.operation;
import static org.assertj.core.api.Assertions.assertThat;

class OperationTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(Operation.class).verify();
    }

    @Test
    void the_balance_after_the_operation_should_be_available() {
        final Operation entry = operation(0, 100_00);

        assertThat(entry.balance()).isEqualTo(100_00);
    }

    @Test
    void the_amount_of_the_operation_should_be_available() {
        final Operation entry = operation(100_00, 0);

        assertThat(entry.amount()).isEqualTo(100_00);
    }

    @Test
    void the_date_of_the_deposit_should_be_set_to_the_current_day() {
        final Operation entry = operation(0, 0);

        assertThat(entry.date()).isEqualTo(LocalDate.now());
    }
}
