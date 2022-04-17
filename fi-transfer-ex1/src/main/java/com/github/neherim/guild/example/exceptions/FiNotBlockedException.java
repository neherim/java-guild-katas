package com.github.neherim.guild.example.exceptions;

public class FiNotBlockedException extends Throwable {
    public FiNotBlockedException(Long orderId) {
        super("Бумаши не были заблокированы по поручению " + orderId);
    }
}
