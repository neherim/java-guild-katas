package com.github.neherim.guild.example.rich.domain.exceptions;


public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(Long accountId) {
        super("Not enough money on the account with id: " + accountId);
    }
}
