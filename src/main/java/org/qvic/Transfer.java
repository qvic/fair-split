package org.qvic;

public record Transfer(Account from, Account to, int amount) {

    public Transfer {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount should be positive");
        }
    }

    @Override
    public String toString() {
        return "%s -> %s (%d)".formatted(from.name(), to.name(), amount);
    }
}
