package com.github.neherim.guild.example.exceptions;


import com.github.neherim.guild.example.entity.Account;

public class FiNotEnoughException extends Exception {
    public FiNotEnoughException(Account account, Integer amountToBlock) {
        super("Нехватает " + amountToBlock + " ценных бумаг на счете " + account.getId());
    }
}
