package com.github.neherim.guild.example.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Account not found. Account id: " + accountId);
    }
}
