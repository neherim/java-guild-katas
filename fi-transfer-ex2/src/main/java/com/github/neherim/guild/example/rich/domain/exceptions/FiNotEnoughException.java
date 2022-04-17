package com.github.neherim.guild.example.rich.domain.exceptions;

import com.github.neherim.guild.example.rich.domain.Account;

public class FiNotEnoughException extends Exception {
    public FiNotEnoughException(Account account, Integer amountToBlock) {
        super("Нехватает " + amountToBlock + " ценных бумаг на счете " + account.getId());
    }
}
