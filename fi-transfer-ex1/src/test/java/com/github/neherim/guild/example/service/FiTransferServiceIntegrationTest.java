package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.FiTechBlockRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotEnoughException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class FiTransferServiceIntegrationTest {
    @Autowired
    private FiTransferService fiTransfer;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FiTechBlockRepository fiTechBlockRepository;

    @Test
    public void testFiTechBlock() throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException {
        // given
        var newAccountId = accountRepository.save(openAccountWithBalance(500)).getId();

        //when
        fiTransfer.makeTechBlock(1L, newAccountId, 100);

        //then
        var fiTechBlock = fiTechBlockRepository.findByOrderIdAndAccountId(1L, newAccountId).orElseThrow();
        assertEquals(100, fiTechBlock.getAmount());
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
        assertEquals(400, accountFrom.getBalance());
        assertEquals(200, accountTo.getBalance());
    }

    private Account openAccountWithBalance(Integer balance) {
        return new Account(null, LocalDateTime.now(), null, balance);
    }
}