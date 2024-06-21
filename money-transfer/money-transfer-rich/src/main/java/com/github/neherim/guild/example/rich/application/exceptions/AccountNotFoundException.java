package com.github.neherim.guild.example.rich.application.exceptions;

import com.github.neherim.guild.example.rich.domain.AccountId;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(AccountId accountId) {
        super("Account not found. Account id: " + accountId);
    }
}
