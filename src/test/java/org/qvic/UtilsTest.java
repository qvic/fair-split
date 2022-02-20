package org.qvic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    public static final Account A = new Account("A");
    public static final Account B = new Account("B");
    public static final Account C = new Account("C");
    public static final Account D = new Account("D");
    public static final Account E = new Account("E");
    public static final Account F = new Account("F");
    public static final Account G = new Account("G");

    @Test
    void separateAdjacencyGroups() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(E, F, 100),
                new Transfer(B, C, 100),
                new Transfer(A, D, 100),
                new Transfer(E, G, 100)
        );

        var result = Utils.separateAdjacencyGroups(transfers);

        assertThat(result).containsExactly(
                List.of(
                        new Transfer(A, B, 100),
                        new Transfer(B, C, 100),
                        new Transfer(A, D, 100)
                ),
                List.of(
                        new Transfer(E, F, 100),
                        new Transfer(E, G, 100)
                ));
    }

    @Test
    void testCalculateBalances() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(B, C, 50),
                new Transfer(C, A, 200),
                new Transfer(A, C, 50)
        );

        var result = Utils.calculateBalances(transfers);

        assertThat(result).containsAllEntriesOf(Map.of(
                A, 50,
                B, 50,
                C, -100
        ));
    }
}