package com.github.neherim.guild.example.exceptions;

public class FiNotBlockedException extends Throwable {
    public FiNotBlockedException(Long orderId) {
        super("Бумаги не были заблокированы по поручению " + orderId);
    }
}
