package com.github.neherim.guild.example.rich.domain.exceptions;

public class MoneyNotReservedException extends RuntimeException {
    public MoneyNotReservedException(Long operationId) {
        super("Money was not reserved for operation with id: " + operationId);
    }
}
