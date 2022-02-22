package org.qvic.model;

import org.qvic.result.Result;

public record Transfer(Account from, Account to, int amount) {

    public Transfer {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount should be positive");
        }
    }

    public static Result<Transfer> fromLine(String line) {
        if (line == null) {
            return Result.err("Line is null");
        }
        var split = line.split(",", 3);
        if (split.length < 3) {
            return Result.err("Malformed line");
        }

        var from = parseAccountName(split[0], "From");
        var to = parseAccountName(split[1], "To");
        var amount = parseAmount(split[2]);

        return Result.coalesce3(from, to, amount,
                (f, t, a) -> new Transfer(new Account(f), new Account(t), a),
                errors -> String.join("; ", errors));
    }

    private static Result<Integer> parseAmount(String str) {
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

    private static Result<String> parseAccountName(String str, String which) {
        var trim = str.trim();
        if (trim.isEmpty()) {
            return Result.err(which + " account name should not be empty");
        } else {
            return Result.ok(trim);
        }
    }

    @Override
    public String toString() {
        return "%s -> %s: %d".formatted(from.name(), to.name(), amount);
    }
}
