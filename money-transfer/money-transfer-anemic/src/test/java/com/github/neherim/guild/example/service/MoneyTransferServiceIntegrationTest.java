package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.MoneyReservationRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.MoneyAlreadyReservedException;
import com.github.neherim.guild.example.exceptions.MoneyNotReservedException;
import com.github.neherim.guild.example.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests with real database instead of mocks
 */
@SpringBootTest
class MoneyTransferServiceIntegrationTest {
    @Autowired
    private MoneyTransferService moneyTransferService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MoneyReservationRepository moneyReservationRepository;

    @Test
    public void testMoneyReservation() throws AccountNotFoundException, NotEnoughMoneyException, MoneyAlreadyReservedException {
        // given
        var newAccountId = accountRepository.save(openAccountWithBalance(500)).getId();

        //when
        moneyTransferService.reserveMoney(1L, newAccountId, 100);

        //then
        var fiTechBlock = moneyReservationRepository.findByOperationIdAndAccountId(1L, newAccountId).orElseThrow();
        assertEquals(100, fiTechBlock.getAmount());
    }


    @Test
    public void testMoneyTransfer() throws AccountNotFoundException, NotEnoughMoneyException, MoneyAlreadyReservedException,
            MoneyNotReservedException {
        // given
        var newAccountFrom = accountRepository.save(openAccountWithBalance(500)).getId();
        var newAccountTo = accountRepository.save(openAccountWithBalance(100)).getId();

        //when
        moneyTransferService.reserveMoney(1L, newAccountFrom, 100);
        moneyTransferService.transferMoney(1L, newAccountFrom, newAccountTo, 100);

        //then
        var accountFrom = accountRepository.findById(newAccountFrom).orElseThrow();
        var accountTo = accountRepository.findById(newAccountTo).orElseThrow();
        assertEquals(400, accountFrom.getBalance());
        assertEquals(200, accountTo.getBalance());
    }

    private Account openAccountWithBalance(Integer balance) {
        return new Account(LocalDateTime.now(), null, balance);
    }
}
