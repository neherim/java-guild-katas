package com.github.neherim.guild.example.rich.domain;

import com.github.neherim.guild.example.rich.domain.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotEnoughException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("На открытом счете не должно быть бумаг")
    public void openAccount() {
        var account = Account.openAccount(LocalDateTime.now());
        assertAll(
                () -> assertEquals(0, account.getBalance()),
                () -> assertEquals(0, account.getNotBlockedBalance()),
                () -> assertNotNull(account.getDateOpen())
        );
    }

    @Test
    @DisplayName("Несколько технических блокировок суммируются при подсчете свободного остатка")
    public void makeSeveralTechBlockTest() throws FiNotEnoughException, FiAlreadyBlockedException {
        var licAcc = openAccountWithBalance(500);
        licAcc.makeTechBlock(1L, 100);
        licAcc.makeTechBlock(2L, 50);

        assertAll(
                () -> assertEquals(500, licAcc.getBalance(),
                        "Реальный остаток не должен меняться при технической блокировке"),
                () -> assertEquals(350, licAcc.getNotBlockedBalance(),
                        "Незаблокированный остаток должен уменьшится на размер блокировок")
        );
    }

    @Test
    @DisplayName("Нельзя по одному поручению сделать две блокировки")
    public void shouldNotBlockFiTwice() throws FiNotEnoughException, FiAlreadyBlockedException {
        var licAcc = openAccountWithBalance(500);
        licAcc.makeTechBlock(1L, 100);
        assertThrows(FiAlreadyBlockedException.class, () -> licAcc.makeTechBlock(1L, 100));
    }

    @Test
    @DisplayName("Произвести техническую блокировку всех бумаг")
    public void blockAllFi() throws FiNotEnoughException, FiAlreadyBlockedException {
        var licAcc = openAccountWithBalance(500);
        licAcc.makeTechBlock(1L, 500);

        assertAll(
                () -> assertEquals(500, licAcc.getBalance(),
                        "Реальный остаток не должен меняться при технической блокировке"),
                () -> assertEquals(0, licAcc.getNotBlockedBalance(),
                        "Незаблокированный остаток должен уменьшится на размер блокировок")
        );
    }

    @Test
    @DisplayName("Ошибка при попытке заблокировать больше бумаг, чем есть незаблокированных бумаг на счете")
    public void notEnoughFiOnAccount() {
        var licAcc = openAccountWithBalance(500);
        assertThrows(FiNotEnoughException.class, () -> licAcc.makeTechBlock(1L, 600));
    }

    private Account openAccountWithBalance(Integer balance) {
        var account = Account.openAccount(LocalDateTime.now());
        account.credit(balance);
        return account;
    }
}