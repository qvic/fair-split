package org.qvic;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {
        var argQueue = new ArrayDeque<>(Arrays.asList(args));
        String inFile = Optional.ofNullable(argQueue.poll())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Input file path was not supplied"));
        String outFile = Optional.ofNullable(argQueue.poll())
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException("Output file path was not supplied"));
        boolean graphOption = Optional.ofNullable(argQueue.poll())
                .map(String::trim)
                .map(s -> s.equals("--graph"))
                .orElse(false);

        var transfers = IOUtils.readFromFile(inFile);
        System.out.printf("[INFO] Read %d transfers from '%s'\n", transfers.size(), inFile);
        var returns = Exchanger.calculateReturnTransfers(transfers);
        IOUtils.writeToFile(outFile, returns);
        System.out.printf("[INFO] Wrote %d return transfers to '%s'\n", returns.size(), outFile);

        if (graphOption) {
            IOUtils.writeGraphDotFile(inFile + ".dot", transfers);
            IOUtils.writeGraphDotFile(outFile + ".dot", returns);
            System.out.println("[INFO] Created .dot digraphs");
        }
    }
}
