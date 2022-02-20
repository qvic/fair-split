package org.qvic;

import org.qvic.model.Account;
import org.qvic.model.AccountBalance;
import org.qvic.model.Transfer;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Exchanger {

    public static List<Transfer> calculateReturnTransfers(List<Transfer> transfers) {
        return GraphUtils.separateAdjacencyGroups(transfers).stream()
                .map(Exchanger::calculateForGroup)
                .flatMap(Collection::stream)
                .toList();
    }

    private static List<Transfer> calculateForGroup(List<Transfer> transfers) {
        Map<Account, Integer> map = BalanceUtils.calculateBalances(transfers);

        PriorityQueue<AccountBalance> creditSources = selectBalancesByPredicate(map, i -> i > 0, i -> i);
        PriorityQueue<AccountBalance> creditDestinations = selectBalancesByPredicate(map, i -> i < 0, i -> -i);

        List<Transfer> returns = new ArrayList<>();

        while (!creditDestinations.isEmpty()) {
            AccountBalance dest = Objects.requireNonNull(creditDestinations.poll());
            int creditsToReturn = dest.balance();

            while (creditsToReturn != 0) {
                AccountBalance source = creditSources.poll();
                if (source == null) throw new AssertionError("Incorrectly calculated balances");

                int transferAmount = Math.min(source.balance(), creditsToReturn);

                if (source.balance() > transferAmount) {
                    creditSources.add(new AccountBalance(source.account(), source.balance() - transferAmount));
                }

                returns.add(new Transfer(source.account(), dest.account(), transferAmount));
                creditsToReturn -= transferAmount;
            }
        }

        return returns;
    }

    private static PriorityQueue<AccountBalance> selectBalancesByPredicate(Map<Account, Integer> map,
                                                                           Predicate<Integer> predicate,
                                                                           Function<Integer, Integer> mapper) {
        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .map(entry -> new AccountBalance(entry.getKey(), mapper.apply(entry.getValue())))
                .collect(Collectors.toCollection(Exchanger::createPriorityQueue));
    }

    private static PriorityQueue<AccountBalance> createPriorityQueue() {
        return new PriorityQueue<>(Comparator.comparing(AccountBalance::balance).reversed());
    }
}
