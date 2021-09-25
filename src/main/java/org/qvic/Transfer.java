package org.qvic;

public record Transfer(Account from, Account to, int amount) {
}
