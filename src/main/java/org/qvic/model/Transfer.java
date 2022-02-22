package org.qvic.model;

import org.qvic.result.Result;

import java.util.ArrayList;
import java.util.List;

public record Transfer(Account from, Account to, int amount) {

    public Transfer {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount should be positive");
        }
    }

    public static Result<Transfer> create(String from, String to, int amount) {
        List<String> errors = new ArrayList<>();
        if (amount <= 0) {
            errors.add("Transfer amount should be positive");
        }
        if (from == null || from.isEmpty()) {
            errors.add("From account name should not be empty");
        }
        if (to == null || to.isEmpty()) {
            errors.add("To account name should not be empty");
        }

        if (errors.isEmpty()) {
            return Result.ok(new Transfer(new Account(from), new Account(to), amount));
        } else {
            return Result.err(String.join("; ", errors));
        }
    }

    @Override
    public String toString() {
        return "%s -> %s: %d".formatted(from.name(), to.name(), amount);
    }
}
