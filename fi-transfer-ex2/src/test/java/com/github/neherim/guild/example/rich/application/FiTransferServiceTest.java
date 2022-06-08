package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.application.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.rich.domain.Account;
import com.github.neherim.guild.example.rich.domain.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotEnoughException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class FiTransferServiceTest {
    @Autowired
    private FiTransferService fiTransfer;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFiTechBlock() throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException {
        // given
        var newAccountId = accountRepository.save(openAccountWithBalance(500)).getId();

        //when
        fiTransfer.makeTechBlock(1L, newAccountId, 100);

        //then
        var account = accountRepository.findById(newAccountId).orElseThrow();
        assertEquals(400, account.getNotBlockedBalance());
    }


    @Test
    public void testFiTransfer() throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException,
            FiNotBlockedException {
        // given
        var newAccountFrom = accountRepository.save(openAccountWithBalance(500)).getId();
        var newAccountTo = accountRepository.save(openAccountWithBalance(100)).getId();

        //when
        fiTransfer.makeTechBlock(1L, newAccountFrom, 100);
        fiTransfer.transferFi(1L, newAccountFrom, newAccountTo, 100);

        //then
        var accountFrom = accountRepository.findById(newAccountFrom).orElseThrow();
        var accountTo = accountRepository.findById(newAccountTo).orElseThrow();
        assertEquals(400, accountFrom.getNotBlockedBalance());
        assertEquals(200, accountTo.getNotBlockedBalance());
    }


    private Account openAccountWithBalance(Integer balance) {
        var account = Account.openAccount(LocalDateTime.now());
        account.credit(balance);
        return account;
    }
}