package org.qvic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Map<Account, Integer> calculateBalances(List<Transfer> transfers) {
        var map = new HashMap<Account, Integer>();
        for (Transfer transfer : transfers) {
            map.put(transfer.to(), map.getOrDefault(transfer.to(), 0) + transfer.amount());
            map.put(transfer.from(), map.getOrDefault(transfer.from(), 0) - transfer.amount());
        }
        return map;
    }
}
