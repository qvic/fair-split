package org.qvic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qvic.TestAccounts.*;

class BalanceUtilsTest {

    @Test
    void testCalculateBalances() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(B, C, 50),
                new Transfer(C, A, 200),
                new Transfer(A, C, 50)
        );

        var result = BalanceUtils.calculateBalances(transfers);

        assertThat(result).containsAllEntriesOf(Map.of(
                A, 50,
                B, 50,
                C, -100
        ));
    }
}