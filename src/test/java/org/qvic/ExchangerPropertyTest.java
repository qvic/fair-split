package org.qvic;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.lifecycle.BeforeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class ExchangerPropertyTest {

    private List<Double> percentages;

    @BeforeProperty
    void beforeProperty() {
        percentages = new ArrayList<>();
    }

    @AfterProperty
    void afterProperty() {
        System.out.printf("Average efficiency percentage: %.2f %%%n", calculateAverage(percentages));
    }

    @Property(tries = 1000)
    boolean everyReturnIsCorrect(@ForAll("transfers") List<Transfer> transfers) {
        List<Transfer> returns = new Exchanger().calculateReturnTransfers(transfers);

        saveStatistics(transfers, returns);

        return isCorrectReturn(transfers, returns);
    }

    @Property(tries = 1000)
    boolean everyReturnIsSmallerThanTransfer(@ForAll("transfers") List<Transfer> transfers) {
        List<Transfer> returns = new Exchanger().calculateReturnTransfers(transfers);

        saveStatistics(transfers, returns);

        return returns.size() <= transfers.size();
    }

    @Provide("transfers")
    Arbitrary<List<Transfer>> transfers() {
        Arbitrary<Account> account = Arbitraries.strings()
                .withCharRange('A', 'Z').ofLength(1)
                .map(Account::new);
        Arbitrary<Integer> amount = Arbitraries.integers()
                .between(1, 100);
        Arbitrary<Transfer> transfer = Combinators.combine(account, account, amount)
                .as(Transfer::new);
        return transfer.list().ofMaxSize(100);
    }

    boolean isCorrectReturn(List<Transfer> transfers, List<Transfer> returns) {
        List<Transfer> all = Stream.concat(transfers.stream(), returns.stream()).toList();
        Map<Account, Integer> map = Utils.calculateBalances(all);
        return map.entrySet().stream()
                .allMatch(entry -> entry.getValue() == 0);
    }

    void saveStatistics(List<Transfer> transfers, List<Transfer> returns) {
        int tSize = transfers.size();
        int rSize = returns.size();
        if (tSize != 0) {
            percentages.add(100.0 * (tSize - rSize) / tSize);
        }
    }

    double calculateAverage(List<Double> percentages) {
        double avg = 0;
        int t = 1;
        for (double x : percentages) {
            avg += (x - avg) / t;
            ++t;
        }
        return avg;
    }
}