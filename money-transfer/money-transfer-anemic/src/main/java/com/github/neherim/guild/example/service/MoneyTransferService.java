package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.MoneyReservationRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.entity.MoneyReservation;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.MoneyAlreadyReservedException;
import com.github.neherim.guild.example.exceptions.MoneyNotReservedException;
import com.github.neherim.guild.example.exceptions.NotEnoughMoneyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final AccountRepository accountRepository;
    private final MoneyReservationRepository moneyReservationRepository;

    /**
     * First step. Reserve money on the account for later transfer
     *
     * @param operationId id of the money transfer operation
     * @param accountId   id of the account on witch we are going to reserve money
     * @param amount      amount of money to reserve
     */
    @Transactional
    public void reserveMoney(Long operationId, Long accountId, Integer amount) {
        // Search for account
        var account = getAccountById(accountId);

        // Check that we haven't yet reserved money for this operation
        var moneyBlockOpt = moneyReservationRepository.findByOperationIdAndAccountId(operationId, accountId);
        if (moneyBlockOpt.isPresent()) {
            throw new MoneyAlreadyReservedException(operationId);
        }

        // Check that there is enough money on the account
        var totalBlockedAmount = moneyReservationRepository.getTotalReservedAmount(accountId);
        if (amount > account.getBalance() - totalBlockedAmount) {
            throw new NotEnoughMoneyException(account.getId());
        }

        // Save new money reservation
        moneyReservationRepository.save(new MoneyReservation(operationId, accountId, amount));
    }

    /**
     * Second step. Remove money reservation and make a transfer
     */
    @Transactional
    public void transferMoney(Long operationId, Long fromAccountId, Long toAccountId, Integer amount) {
        // Search for accounts
        var fromAccount = getAccountById(fromAccountId);
        var toAccount = getAccountById(toAccountId);

        // Search for money reservation by operation id and account id
        var moneyReservation = moneyReservationRepository.findByOperationIdAndAccountId(operationId, fromAccountId)
                .orElseThrow(() -> new MoneyNotReservedException(operationId));

        // Remove reservation
        moneyReservationRepository.delete(moneyReservation);
        // Withdraw money
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        // Deposit money
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    private Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
