package com.github.neherim.guild.example.rich.domain.exceptions;

public class FiNotBlockedException extends FiTransferException {
    public FiNotBlockedException(Long orderId) {
        super("Бумаги не были заблокированы по поручению " + orderId);
    }
}
