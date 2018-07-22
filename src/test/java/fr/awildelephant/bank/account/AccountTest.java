package fr.awildelephant.bank.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static fr.awildelephant.bank.account.Operation.operation;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private static final long TWO_EUROS = 2_00;
    private static final long ONE_EURO = 1_00;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(now());
    }

    @Test
    void the_user_should_be_able_to_make_a_deposit() {
        account.deposit(TWO_EUROS);

        assertThat(account.balance()).isEqualTo(TWO_EUROS);
    }

    @Test
    void the_user_should_not_be_able_to_deposit_nil_value() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
    }

    @Test
    void the_user_should_not_be_able_to_deposit_a_negative_amount_of_money() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-1));
    }

    @Test
    void the_user_should_be_able_to_withdraw_money_from_the_account() {
        account.deposit(TWO_EUROS);
        account.withdraw(ONE_EURO);

        assertThat(account.balance()).isEqualTo(ONE_EURO);
    }

    @Test
    void the_user_should_not_be_able_to_withdraw_nil_value() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
    }

    @Test
    void the_user_should_not_be_able_to_withdraw_a_negative_amount_of_money() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1));
    }

    @Test
    void the_user_should_not_be_able_to_withdraw_more_money_than_there_is_in_their_account() {
        account.deposit(ONE_EURO);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(TWO_EUROS));
    }

    @Test
    void it_should_throw_an_exception_if_the_user_tries_to_create_a_statement_for_the_future() {
        assertThrows(IllegalArgumentException.class, () -> account.statement(now().minusDays(1), Period.ofDays(2)));
    }
}