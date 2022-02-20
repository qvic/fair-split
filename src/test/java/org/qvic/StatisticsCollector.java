package org.qvic;

import java.util.ArrayList;
import java.util.List;

class StatisticsCollector {

    private final List<Double> percentages = new ArrayList<>();

    public void print() {
        System.out.printf("Average efficiency percentage: %.2f%%%n", calculateAverage(percentages));
    }

    public void collect(List<Transfer> transfers, List<Transfer> returns) {
        int tSize = transfers.size();
        int rSize = returns.size();
        if (tSize != 0) {
            percentages.add(100.0 * (tSize - rSize) / tSize);
        }
    }

    private double calculateAverage(List<Double> percentages) {
        double avg = 0;
        int t = 1;
        for (double x : percentages) {
            avg += (x - avg) / t;
            ++t;
        }
        return avg;
    }
}
