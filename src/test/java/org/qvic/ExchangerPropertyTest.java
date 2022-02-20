package org.qvic;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.qvic.model.Account;
import org.qvic.model.Transfer;

import java.util.List;

import static org.qvic.BalanceUtils.isCorrectReturn;

class ExchangerPropertyTest {

    private StatisticsCollector stats;

    @BeforeProperty
    void beforeProperty() {
        stats = new StatisticsCollector();
    }

    @AfterProperty
    void afterProperty() {
        stats.print();
    }

    @Property(tries = 1000)
    boolean everyReturnIsCorrect(@ForAll("transfers") List<Transfer> transfers) {
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        stats.collect(transfers, returns);

        return isCorrectReturn(transfers, returns);
    }

    @Property(tries = 1000)
    boolean everyReturnIsSmallerThanTransfer(@ForAll("transfers") List<Transfer> transfers) {
        List<Transfer> returns = Exchanger.calculateReturnTransfers(transfers);

        stats.collect(transfers, returns);

        return returns.size() <= transfers.size();
    }

    @Provide("transfers")
    Arbitrary<List<Transfer>> transfers() {
        Arbitrary<Account> account = Arbitraries.integers()
                .between(1, 10)
                .map(Object::toString)
                .map(Account::new);
        Arbitrary<Integer> amount = Arbitraries.integers()
                .between(1, 100);
        Arbitrary<Transfer> transfer = Combinators.combine(account, account, amount)
                .as(Transfer::new);
        return transfer.list().ofMaxSize(1000);
    }
}