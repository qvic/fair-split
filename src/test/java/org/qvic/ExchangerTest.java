package org.qvic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangerTest {

    @Test
    void testEmpty() {
        List<Transfer> transfers = List.of();
        List<Transfer> returns = new Exchanger().calculateReturnTransfers(transfers);

        assertThat(returns).isEmpty();
    }

    @Test
    void testSingle() {
        List<Transfer> transfers = List.of(new Transfer(new Account("1"), new Account("2"), 100));
        List<Transfer> returns = new Exchanger().calculateReturnTransfers(transfers);

        assertThat(returns).containsExactly(new Transfer(new Account("2"), new Account("1"), 100));
        assertTrue(hasPositiveAmountTransfers(returns));
        assertTrue(isCorrectReturn(transfers, returns));
    }

    @Test
    void testCircular() {
        List<Transfer> transfers = List.of(
                new Transfer(new Account("1"), new Account("2"), 100),
                new Transfer(new Account("2"), new Account("3"), 100),
                new Transfer(new Account("3"), new Account("1"), 100)
        );
        List<Transfer> returns = new Exchanger().calculateReturnTransfers(transfers);

        assertTrue(hasPositiveAmountTransfers(returns));
        assertTrue(isCorrectReturn(transfers, returns));
    }

    boolean hasPositiveAmountTransfers(List<Transfer> transfers) {
        return transfers.stream()
                .allMatch(transfer -> transfer.amount() > 0);
    }

    boolean isCorrectReturn(List<Transfer> transfers, List<Transfer> returns) {
        List<Transfer> all = Stream.concat(transfers.stream(), returns.stream()).toList();
        Map<Account, Integer> map = Utils.calculateBalances(all);
        return map.entrySet().stream()
                .allMatch(entry -> entry.getValue() == 0);
    }
}