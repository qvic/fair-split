package org.qvic;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Not enough arguments: input and output filenames should be passed");
            System.exit(1);
        }

        var inFile = args[0];
        var outFile = args[1];

        var transfers = IOUtils.readFromFile(inFile);
        var returns = Exchanger.calculateReturnTransfers(transfers);
        IOUtils.writeToFile(outFile, returns);

        System.out.println("OK");
    }
}
