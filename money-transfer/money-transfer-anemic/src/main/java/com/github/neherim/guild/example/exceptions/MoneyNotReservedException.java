package com.github.neherim.guild.example.exceptions;

public class MoneyNotReservedException extends RuntimeException {
    public MoneyNotReservedException(Long operationId) {
        super("Money was not reserved for operation with id: " + operationId);
    }
}
