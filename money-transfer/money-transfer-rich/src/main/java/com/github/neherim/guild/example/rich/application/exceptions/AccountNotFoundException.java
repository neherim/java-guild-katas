package com.github.neherim.guild.example.rich.application.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Account not found. Account id: " + accountId);
    }
}
