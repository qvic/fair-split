package org.qvic;

import java.util.*;
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

        // use treemap
        TreeSet<AccountBalance> creditSources = selectBalancesByPredicate(map, i -> i > 0);
        TreeSet<AccountBalance> creditDestinations = selectBalancesByPredicate(map, i -> i < 0);

        List<Transfer> returns = new ArrayList<>();

        while (!creditDestinations.isEmpty()) {
            AccountBalance dest = Objects.requireNonNull(creditDestinations.pollLast());
            int creditsToReturn = dest.balance();

            while (creditsToReturn != 0) {
                AccountBalance source = creditSources.ceiling(new AccountBalance(new Account(""), creditsToReturn));
                if (source == null) source = creditSources.last();
                creditSources.remove(source);

                int transferAmount = Math.min(source.balance(), creditsToReturn);

                if (source.balance() > transferAmount) {
                    creditSources.add(new AccountBalance(source.account(), source.balance() - transferAmount));
                }

                returns.add(new Transfer(source.account(), dest.account(), transferAmount));
                creditsToReturn -= transferAmount;
            }

//            System.out.printf("Returned all credits to %s, returns: %s%n", dest.account(), returns);
        }

        return returns;
    }

    private static TreeSet<AccountBalance> selectBalancesByPredicate(Map<Account, Integer> map, Predicate<Integer> predicate) {
        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .map(entry -> new AccountBalance(entry.getKey(), Math.abs(entry.getValue())))
                .collect(Collectors.toCollection(Exchanger::createTreeSet));
    }

    private static TreeSet<AccountBalance> createTreeSet() {
        return new TreeSet<>(Comparator.comparing(AccountBalance::balance)
                .thenComparing(AccountBalance::account));
    }
}
