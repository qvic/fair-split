package org.qvic;

import org.qvic.model.Transfer;
import org.qvic.result.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IOUtils {

    public static List<Transfer> readFromFile(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename)).stream()
                .filter(line -> !line.isEmpty())
                .<Transfer>mapMulti((line, consumer) -> {
                    var result = IOUtils.fromLine(line);
                    result.consume(consumer,
                            err -> System.out.printf("[WARN] Could not parse line '%s': %s\n", line, err));
                })
                .toList();
    }

    private static Result<Transfer> fromLine(String line) {
        var split = line.split(",", 3);
        if (split.length < 3) {
            return Result.err("Malformed line");
        }
        var source = split[0].trim();
        var destination = split[1].trim();
        try {
            var amount = Integer.parseInt(split[2].trim());
            return Transfer.create(source, destination, amount);
        } catch (NumberFormatException e) {
            return Result.err("Could not parse amount of transfer");
        }
    }

    public static void writeToFile(String filename, List<Transfer> transfers) throws IOException {
        var lines = transfers.stream().map(IOUtils::asString).toList();
        Files.write(Path.of(filename), lines);
    }

    private static String asString(Transfer transfer) {
        return "%s, %s, %d".formatted(transfer.from().name(), transfer.to().name(), transfer.amount());
    }

    public static void writeGraphDotFile(String filename, List<Transfer> transfers) throws IOException {
        var result = new StringBuilder();
        result.append("digraph {\n");
        for (Transfer t : transfers) {
            result.append("\"%s\" -> \"%s\" [ label=\"%d\" ];\n".formatted(t.from().name(), t.to().name(), t.amount()));
        }
        result.append("}");
        Files.writeString(Path.of(filename), result);
    }
}
