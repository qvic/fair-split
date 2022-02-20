package org.qvic;

import org.junit.jupiter.api.Test;
import org.qvic.model.Transfer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.qvic.TestAccounts.*;

class GraphUtilsTest {

    @Test
    void testSeparateAdjacencyGroupsEmpty() {
        List<Transfer> transfers = List.of();

        var result = GraphUtils.separateAdjacencyGroups(transfers);

        assertThat(result).isEmpty();
    }

    @Test
    void testSeparateAdjacencyGroupsSingle() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100)
        );

        var result = GraphUtils.separateAdjacencyGroups(transfers);

        assertThat(result).containsExactly(List.of(
                new Transfer(A, B, 100)
        ));
    }

    @Test
    void testSeparateAdjacencyGroups() {
        List<Transfer> transfers = List.of(
                new Transfer(A, B, 100),
                new Transfer(E, F, 100),
                new Transfer(B, C, 100),
                new Transfer(A, D, 100),
                new Transfer(E, G, 100)
        );

        var result = GraphUtils.separateAdjacencyGroups(transfers);

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
}