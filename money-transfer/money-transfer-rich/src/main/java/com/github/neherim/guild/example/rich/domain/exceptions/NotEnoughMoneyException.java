package com.github.neherim.guild.example.rich.domain.exceptions;


import com.github.neherim.guild.example.rich.domain.AccountId;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(AccountId accountId) {
        super("Not enough money on the account with id: " + accountId);
    }
}
