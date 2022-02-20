package org.qvic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.qvic.BalanceUtils.isCorrectReturn;
import static org.qvic.TestAccounts.*;


class ExchangerTest {


    @Test
    void testEmpty() {
        List<Transfer> transfers = List.of();
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        assertThat(returns).isEmpty();
    }

    @Test
    void testSingle() {
        List<Transfer> transfers = List.of(new Transfer(A, B, 100));
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        assertThat(returns).containsExactly(new Transfer(B, A, 100));
    }

    @Test
    void testCircular() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(B, C, 100),
                new Transfer(C, A, 100)
        );
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        assertTrue(isCorrectReturn(transfers, returns));
        assertThat(returns.size()).isLessThanOrEqualTo(transfers.size());
    }

    @Test
    void testOther() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(B, C, 50),
                new Transfer(C, A, 200),
                new Transfer(A, C, 50)
        );
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        assertTrue(isCorrectReturn(transfers, returns));
        assertThat(returns.size()).isLessThanOrEqualTo(transfers.size());
    }

    @Test
    void testReturnsShouldBeShorter() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 1),
                new Transfer(A, C, 1),
                new Transfer(A, D, 3),
                new Transfer(E, F, 3)
        );
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        System.out.println(returns);

        assertTrue(isCorrectReturn(transfers, returns));
        assertThat(returns.size()).isLessThanOrEqualTo(transfers.size());
    }
}