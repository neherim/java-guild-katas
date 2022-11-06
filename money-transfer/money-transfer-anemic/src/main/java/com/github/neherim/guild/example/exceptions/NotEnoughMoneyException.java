package com.github.neherim.guild.example.exceptions;


public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(Long accountId) {
        super("Not enough money on the account with id: " + accountId);
    }
}
