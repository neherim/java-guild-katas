package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.FiTechBlockRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.entity.FiTechBlock;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotEnoughException;
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
class FiTransferServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private FiTechBlockRepository fiTechBlockRepository;
    @Captor
    private ArgumentCaptor<FiTechBlock> fiTechBlockCaptor;

    private FiTransferService fiTransferService;


    @BeforeEach
    public void setup() {
        fiTransferService = new FiTransferService(accountRepository, fiTechBlockRepository);
    }

    @Test
    @DisplayName("Проверяем возможность технической блокировки")
    void makeTechBlock() throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException {
        // given
        var account = openAccountWithBalance(1L, 500);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(fiTechBlockRepository.findByOrderIdAndAccountId(any(), any())).thenReturn(Optional.empty());

        // when
        fiTransferService.makeTechBlock(100L, 1L, 300);

        // then
        // Захватываем аргумент в моках, для дальнейшей проверки
        Mockito.verify(fiTechBlockRepository).save(fiTechBlockCaptor.capture());
        assertEquals(fiTechBlockCaptor.getValue().getAmount(), 300, "Должно быть заблокировано 300 бумаг");
        assertEquals(fiTechBlockCaptor.getValue().getOrderId(), 100L, "Должно быть заблокировано 300 бумаг");
    }

    @Test
    @DisplayName("Нельзя заблокировать больше бумаг, чем есть на счете")
    void checkNotEnoughException() {
        // given
        var account = openAccountWithBalance(1L, 500);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(fiTechBlockRepository.getTotalBlockedAmount(1L)).thenReturn(400); // на счету уже заблокировано 400 бумаг

        // then
        assertThrows(FiNotEnoughException.class, () -> fiTransferService.makeTechBlock(100L, 1L, 300));
    }

    @Test
    @DisplayName("Успешный перевод бумаг")
    void testFiTransfer() throws FiNotBlockedException, AccountNotFoundException {
        // given
        var debitAccount = openAccountWithBalance(1L, 500);
        var creditAccount = openAccountWithBalance(2L, 100);
        var blocked = new FiTechBlock(null, 100L, 1L, 200);
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(debitAccount));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(creditAccount));
        Mockito.when(fiTechBlockRepository.findByOrderIdAndAccountId(100L, 1L)).thenReturn(Optional.of(blocked));

        // when
        fiTransferService.transferFi(100L, 1L, 2L, 100);

        // then
        Mockito.verify(fiTechBlockRepository).delete(blocked);
        assertEquals(400, debitAccount.getBalance(), "Баланс счета дебита не совпадает с ожиданием");
        assertEquals(200, creditAccount.getBalance(), "Баланс счета кредита не совпадает с ожиданием");
    }

    private Account openAccountWithBalance(Long id, Integer balance) {
        return new Account(id, LocalDateTime.now(), null, balance);
    }
}