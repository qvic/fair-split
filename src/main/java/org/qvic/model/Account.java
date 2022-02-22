package org.qvic.model;

public record Account(String name) implements Comparable<Account> {

    public Account {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Account name should be non-empty");
        }
    }

    @Override
    public int compareTo(Account a) {
        return name.compareTo(a.name);
    }
}
