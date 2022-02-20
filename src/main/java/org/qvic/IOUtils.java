package org.qvic;

import org.qvic.model.Account;
import org.qvic.model.Transfer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IOUtils {

    public static List<Transfer> readFromFile(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename)).stream()
                .map(IOUtils::fromLine)
                .toList();
    }

    private static Transfer fromLine(String line) {
        var split = line.split(",", 3);
        if (split.length < 3) {
            throw new IllegalArgumentException("Invalid format of input file");
        }
        var source = split[0].trim();
        var destination = split[1].trim();
        var amount = Integer.parseInt(split[2].trim());
        return new Transfer(new Account(source), new Account(destination), amount);
    }

    public static void writeToFile(String filename, List<Transfer> transfers) throws IOException {
        var lines = transfers.stream().map(IOUtils::asString).toList();
        Files.write(Path.of(filename), lines);
    }

    private static String asString(Transfer transfer) {
        return "%s, %s, %d".formatted(transfer.from().name(), transfer.to().name(), transfer.amount());
    }
}
