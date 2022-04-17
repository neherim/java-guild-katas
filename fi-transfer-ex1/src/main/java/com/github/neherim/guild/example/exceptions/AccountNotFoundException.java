package com.github.neherim.guild.example.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(Long accountId) {
        super("Счет не найден " + accountId);
    }
}
