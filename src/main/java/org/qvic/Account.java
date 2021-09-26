package org.qvic;

public record Account(String name) implements Comparable<Account> {

    @Override
    public int compareTo(Account a) {
        return name.compareTo(a.name);
    }
}
