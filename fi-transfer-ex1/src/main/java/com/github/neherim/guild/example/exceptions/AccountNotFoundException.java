package com.github.neherim.guild.example.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Счет не найден " + accountId);
    }
}
