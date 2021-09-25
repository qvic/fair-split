package org.qvic;

import java.util.List;

public class Exchanger {

    public List<Transfer> calculateReturnTransfers(List<Transfer> transfers) {
        return transfers.stream()
                .map(t -> new Transfer(t.to(), t.from(), t.amount()))
                .toList();
    }
}
