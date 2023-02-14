package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class MoneyTransferServiceIntegrationTest {
    @Autowired
    private MoneyTransferService moneyTransferService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFiTechBlock() {
        // given
        var newAccountId = accountRepository.save(openAccountWithBalance(500)).getId();

        //when
        moneyTransferService.reserveMoney(1L, newAccountId, 100);

        //then
        var account = accountRepository.findById(newAccountId).orElseThrow();
        assertEquals(400, account.getAvailableBalance());
    }


    @Test
    public void testFiTransfer() {
        // given
        var newAccountFrom = accountRepository.save(openAccountWithBalance(500)).getId();
        var newAccountTo = accountRepository.save(openAccountWithBalance(100)).getId();

        //when
        moneyTransferService.reserveMoney(1L, newAccountFrom, 100);
        moneyTransferService.transferMoney(1L, newAccountFrom, newAccountTo);

        //then
        var accountFrom = accountRepository.findById(newAccountFrom).orElseThrow();
        var accountTo = accountRepository.findById(newAccountTo).orElseThrow();
        assertEquals(400, accountFrom.getAvailableBalance());
        assertEquals(200, accountTo.getAvailableBalance());
    }


    private Account openAccountWithBalance(Integer balance) {
        var account = Account.openAccount(LocalDateTime.now());
        account.deposit(balance);
        return account;
    }
}