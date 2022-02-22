package org.qvic.util;

import org.qvic.model.Account;
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
                    var result = parseTransfer(line);
                    result.consume(consumer,
                            err -> System.out.printf("[WARN] Could not parse line '%s': %s\n", line, err));
                })
                .toList();
    }

    public static Result<Transfer> parseTransfer(String line) {
        if (line == null) {
            return Result.err("Line is null");
        }
        var split = line.split(",", 3);
        if (split.length < 3) {
            return Result.err("Malformed line");
        }

        var from = parseTransferAccountName(split[0], "From");
        var to = parseTransferAccountName(split[1], "To");
        var amount = parseTransferAmount(split[2]);

        return Result.coalesce3(from, to, amount,
                (f, t, a) -> new Transfer(new Account(f), new Account(t), a),
                errors -> String.join("; ", errors));
    }

    private static Result<Integer> parseTransferAmount(String str) {
        try {
            var amount = Integer.parseInt(str.trim());
            if (amount <= 0) {
                return Result.err("Transfer amount should be positive");
            }
            return Result.ok(amount);
        } catch (NumberFormatException e) {
            return Result.err("Could not parse transfer amount");
        }
    }

    private static Result<String> parseTransferAccountName(String str, String which) {
        var trim = str.trim();
        if (trim.isEmpty()) {
            return Result.err(which + " account name should not be empty");
        } else {
            return Result.ok(trim);
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
