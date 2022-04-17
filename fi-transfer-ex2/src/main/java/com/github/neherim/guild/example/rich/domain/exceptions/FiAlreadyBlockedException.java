package com.github.neherim.guild.example.rich.domain.exceptions;

import com.github.neherim.guild.example.rich.domain.Account;

public class FiAlreadyBlockedException extends Exception {
    public FiAlreadyBlockedException(Account account, Long orderId) {
        super("Бумаги уже были заблокированы на счете" + account.getId() + " по поручению " + orderId);
    }
}
