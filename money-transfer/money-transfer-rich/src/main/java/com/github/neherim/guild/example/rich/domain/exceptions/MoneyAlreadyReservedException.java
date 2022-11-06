package com.github.neherim.guild.example.rich.domain.exceptions;


public class MoneyAlreadyReservedException extends RuntimeException {
    public MoneyAlreadyReservedException(Long operationId) {
        super("Money has already been reserved for operation with id: " + operationId);
    }
}
