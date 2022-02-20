package org.qvic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphUtils {

    public static List<List<Transfer>> separateAdjacencyGroups(List<Transfer> transfers) {
        if (transfers.isEmpty()) {
            return List.of();
        }

        List<Set<Account>> adjacencyGroups = new ArrayList<>();
        List<List<Transfer>> resultTransfers = new ArrayList<>();

        for (Transfer transfer : transfers) {

            boolean foundGroup = false;
            int index = 0;

            for (Set<Account> group : adjacencyGroups) {
                List<Transfer> resultTransfersGroup = resultTransfers.get(index);

                if (group.contains(transfer.to()) || group.contains(transfer.from())) {
                    addTransfer(transfer, group, resultTransfersGroup);
                    foundGroup = true;
                    break;
                }
                index++;
            }

            if (!foundGroup) {
                var newGroup = new HashSet<Account>();
                adjacencyGroups.add(newGroup);
                var newTransfers = new ArrayList<Transfer>();
                resultTransfers.add(newTransfers);

                addTransfer(transfer, newGroup, newTransfers);
            }
        }

        return resultTransfers;
    }

    private static void addTransfer(Transfer t, Set<Account> group, List<Transfer> transfers) {
        group.add(t.to());
        group.add(t.from());
        transfers.add(t);
    }
}
