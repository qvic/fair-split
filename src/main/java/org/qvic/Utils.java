package org.qvic;

import java.util.*;

public class Utils {

    public static List<List<Transfer>> separateAdjacencyGroups(List<Transfer> transfers) {
        if (transfers.isEmpty()) {
            return List.of();
        }

        List<Set<Account>> adjacencyGroups = new ArrayList<>();
        List<List<Transfer>> resultTransfers = new ArrayList<>();

        var initTransfer = transfers.get(0);

        var initGroup = new HashSet<Account>();
        initGroup.add(initTransfer.to());
        initGroup.add(initTransfer.from());
        adjacencyGroups.add(initGroup);

        var initTransfers = new ArrayList<Transfer>();
        initTransfers.add(initTransfer);
        resultTransfers.add(initTransfers);

        var transfersTail = transfers.stream().skip(1).toList();

        for (Transfer transfer : transfersTail) {
            boolean found = false;
            int index = 0;
            for (Set<Account> group : adjacencyGroups) {
                List<Transfer> resultTransfersGroup = resultTransfers.get(index);
                if (group.contains(transfer.to()) || group.contains(transfer.from())) {
                    group.add(transfer.to());
                    group.add(transfer.from());
                    resultTransfersGroup.add(transfer);
                    found = true;
                    break;
                }
                index++;
            }

            if (!found) {
                var newGroup = new HashSet<Account>();
                newGroup.add(transfer.to());
                newGroup.add(transfer.from());
                adjacencyGroups.add(newGroup);

                var newTransfers = new ArrayList<Transfer>();
                newTransfers.add(transfer);
                resultTransfers.add(newTransfers);
            }
        }

        return resultTransfers;
    }


    public static Map<Account, Integer> calculateBalances(List<Transfer> transfers) {
        var map = new HashMap<Account, Integer>();
        for (Transfer transfer : transfers) {
            map.put(transfer.to(), map.getOrDefault(transfer.to(), 0) + transfer.amount());
            map.put(transfer.from(), map.getOrDefault(transfer.from(), 0) - transfer.amount());
        }
        return map;
    }
}
