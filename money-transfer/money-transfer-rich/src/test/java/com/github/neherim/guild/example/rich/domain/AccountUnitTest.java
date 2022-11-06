package com.github.neherim.guild.example.rich.domain;

import com.github.neherim.guild.example.rich.domain.exceptions.MoneyAlreadyReservedException;
import com.github.neherim.guild.example.rich.domain.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountUnitTest {

    @Test
    @DisplayName("There must be zero balance on the new account")
    public void openAccount() {
        var account = Account.openAccount(LocalDateTime.now());
        assertAll(
                () -> assertEquals(0, account.getTotalBalance()),
                () -> assertEquals(0, account.getAvailableBalance())
        );
    }

    @Test
    @DisplayName("Multiple reservations should be summed up")
    public void makeMultipleMoneyReservations() {
        var account = openAccountWithBalance(500);
        account.reserveMoney(1L, 100);
        account.reserveMoney(2L, 50);

        assertAll(
                () -> assertEquals(500, account.getTotalBalance(), "Total balance should not change after money reservation"),
                () -> assertEquals(350, account.getAvailableBalance(), "Available balance should decrease after money reservation")
        );
    }

    @Test
    @DisplayName("Shouldn't be possible to reserve money for one operation more than once")
    public void shouldNotBlockMoneyTwice() {
        var account = openAccountWithBalance(500);
        account.reserveMoney(1L, 100);
        assertThrows(MoneyAlreadyReservedException.class, () -> account.reserveMoney(1L, 100));
    }

    @Test
    @DisplayName("It is possible to reserve all the money on the account at once")
    public void reserveAllMoney() {
        var account = openAccountWithBalance(500);
        account.reserveMoney(1L, 500);

        assertAll(
                () -> assertEquals(500, account.getTotalBalance(), "Total balance should not change after money reservation"),
                () -> assertEquals(0, account.getAvailableBalance(), "Available balance must be zero")
        );
    }

    @Test
    @DisplayName("You can't reserve more money than is on the account")
    public void notEnoughFiOnAccount() {
        var account = openAccountWithBalance(500);
        assertThrows(NotEnoughMoneyException.class, () -> account.reserveMoney(1L, 600));
    }

    private Account openAccountWithBalance(Integer balance) {
        var account = Account.openAccount(LocalDateTime.now());
        account.deposit(balance);
        return account;
    }
}