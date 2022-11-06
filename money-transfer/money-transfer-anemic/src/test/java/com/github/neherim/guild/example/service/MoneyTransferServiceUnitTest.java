package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.MoneyReservationRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.entity.MoneyReservation;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.MoneyAlreadyReservedException;
import com.github.neherim.guild.example.exceptions.MoneyNotReservedException;
import com.github.neherim.guild.example.exceptions.NotEnoughMoneyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceUnitTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MoneyReservationRepository moneyReservationRepository;
    @Captor
    private ArgumentCaptor<MoneyReservation> moneyReservationArgumentCaptor;

    private MoneyTransferService moneyTransferService;


    @BeforeEach
    public void setup() {
        moneyTransferService = new MoneyTransferService(accountRepository, moneyReservationRepository);
    }

    @Test
    @DisplayName("Check if it is possible to reserve money")
    void successfulMoneyReservation() throws AccountNotFoundException, NotEnoughMoneyException, MoneyAlreadyReservedException {
        // given
        var account = openAccountWithBalance(500);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(moneyReservationRepository.findByOperationIdAndAccountId(any(), any())).thenReturn(Optional.empty());

        // when
        moneyTransferService.reserveMoney(100L, 1L, 300);

        // then
        Mockito.verify(moneyReservationRepository).save(moneyReservationArgumentCaptor.capture());
        var savedMoneyReservation = moneyReservationArgumentCaptor.getValue();

        assertEquals(300, savedMoneyReservation.getAmount());
        assertEquals(100L, savedMoneyReservation.getOperationId());
    }

    @Test
    @DisplayName("You can't reserve more money than is on the account")
    void checkNotEnoughMoneyException() {
        var operationId = 100L;
        var accountId = 1L;
        // given
        var account = openAccountWithBalance(500);

        // when
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(moneyReservationRepository.getTotalReservedAmount(accountId)).thenReturn(400);


        // then
        assertThrows(NotEnoughMoneyException.class, () -> moneyTransferService.reserveMoney(operationId, accountId, 300));
    }

    @Test
    @DisplayName("Successful money transfer")
    void successfulMoneyTransfer() throws MoneyNotReservedException, AccountNotFoundException {
        // given
        var debitAccount = openAccountWithBalance(500);
        var creditAccount = openAccountWithBalance(100);
        var blocked = new MoneyReservation(100L, 1L, 200);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(debitAccount));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(creditAccount));
        Mockito.when(moneyReservationRepository.findByOperationIdAndAccountId(100L, 1L)).thenReturn(Optional.of(blocked));

        // when
        moneyTransferService.transferMoney(100L, 1L, 2L, 100);

        // then
        Mockito.verify(moneyReservationRepository).delete(blocked);
        assertEquals(400, debitAccount.getBalance());
        assertEquals(200, creditAccount.getBalance());
    }

    private Account openAccountWithBalance(Integer balance) {
        return new Account(LocalDateTime.now(), null, balance);
    }
}