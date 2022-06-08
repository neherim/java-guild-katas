package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.application.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.rich.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FiTransferService {
    private final AccountRepository accountRepository;

    /**
     * Провести техническую блокировку
     *
     * @param orderId   поручение на основании которого блокируем бумаги
     * @param accountId id счета
     * @param amount    количество бумаг для блокирования
     */
    @Transactional
    public void makeTechBlock(Long orderId, Long accountId, Integer amount) {
        var account = getAccountById(accountId);
        account.makeTechBlock(orderId, amount);
    }

    @Transactional
    public void transferFi(Long orderId, Long debitAccountId, Long creditAccountId, Integer amount) {
        var debitAccount = getAccountById(debitAccountId);
        var creditAccount = getAccountById(creditAccountId);
        debitAccount.debit(orderId);
        creditAccount.credit(amount);
    }

    private Account getAccountById(Long id) {
        return accountRepository.findByIdWithLock(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
