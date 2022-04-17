package com.github.neherim.guild.example.exceptions;


import com.github.neherim.guild.example.entity.Account;

public class FiAlreadyBlockedException extends Exception {
    public FiAlreadyBlockedException(Account account, Long orderId) {
        super("Бумаги уже были заблокированы на счете" + account.getId() + " по поручению " + orderId);
    }
}
