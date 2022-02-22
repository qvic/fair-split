package org.qvic.util;

import org.qvic.model.Account;
import org.qvic.model.Transfer;

import java.util.*;
import java.util.stream.Stream;

public class BalanceUtils {

    public static Map<Account, Integer> calculateBalances(List<Transfer> transfers) {
        var map = new HashMap<Account, Integer>();
        for (Transfer transfer : transfers) {
            map.put(transfer.to(), Math.addExact(map.getOrDefault(transfer.to(), 0), transfer.amount()));
            map.put(transfer.from(), Math.subtractExact(map.getOrDefault(transfer.from(), 0), transfer.amount()));
        }
        return map;
    }

    public static boolean isCorrectReturn(List<Transfer> transfers, List<Transfer> returns) {
        List<Transfer> all = Stream.concat(transfers.stream(), returns.stream()).toList();
        Map<Account, Integer> map = BalanceUtils.calculateBalances(all);
        return map.entrySet().stream()
                .allMatch(entry -> entry.getValue() == 0);
    }
}
