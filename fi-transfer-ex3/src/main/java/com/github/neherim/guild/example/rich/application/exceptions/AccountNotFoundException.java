package com.github.neherim.guild.example.rich.application.exceptions;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(Long accountId) {
        super("Счет не найден " + accountId);
    }
}
