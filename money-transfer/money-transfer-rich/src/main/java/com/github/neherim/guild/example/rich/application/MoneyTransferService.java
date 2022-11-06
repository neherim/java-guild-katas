package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.application.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.rich.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final AccountRepository accountRepository;

    /**
     * First step. Reserve money on the account for later transfer
     *
     * @param operationId id of the money transfer operation
     * @param accountId   id of the account on witch we are going to reserve money
     * @param amount      amount of money to reserve
     */
    @Transactional
    public void reserveMoney(Long operationId, Long accountId, Integer amount) {
        var account = getAccountById(accountId);
        account.reserveMoney(operationId, amount);
    }

    /**
     * Second step. Remove money reservation and make a transfer
     */
    @Transactional
    public void transferMoney(Long operationId, Long fromAccountId, Long toAccountId) {
        var debitAccount = getAccountById(fromAccountId);
        var creditAccount = getAccountById(toAccountId);
        var withdrawAmount = debitAccount.withdrawReserved(operationId);
        creditAccount.deposit(withdrawAmount);
    }

    private Account getAccountById(Long id) {
        return accountRepository.findByIdWithLock(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
