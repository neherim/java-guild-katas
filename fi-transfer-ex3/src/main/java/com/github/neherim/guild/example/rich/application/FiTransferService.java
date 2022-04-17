package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.application.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.rich.application.repository.AccountRepository;
import com.github.neherim.guild.example.rich.application.repository.FiTechBlockRepository;
import com.github.neherim.guild.example.rich.domain.Account;
import com.github.neherim.guild.example.rich.domain.FiTechBlock;
import com.github.neherim.guild.example.rich.domain.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotEnoughException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FiTransferService {
    private final AccountRepository accountRepository;
    private final FiTechBlockRepository fiTechBlockRepository;

    /**
     * Провести техническую блокировку
     *
     * @param orderId   поручение на основании которого блокируем бумаги
     * @param accountId id счета
     * @param amount    количество бумаг для блокирования
     * @throws FiNotEnoughException недостаточно ц.б. для проведения технической блокировки
     */
    @Transactional
    public void makeTechBlock(Long orderId, Long accountId, Integer amount)
            throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException {
        var account = getAccountById(accountId);
        FiTechBlock fiTechBlock = account.makeTechBlock(orderId, amount);
        fiTechBlockRepository.save(fiTechBlock);
    }

    @Transactional
    public void transferFi(Long orderId, Long debitAccountId, Long creditAccountId, Integer amount)
            throws AccountNotFoundException, FiNotBlockedException {
        if (debitAccountId.equals(creditAccountId)) {
            throw new IllegalArgumentException("Счет списания совпадает со счетом зачисления");
        }
        var debitAccount = getAccountById(debitAccountId);
        var creditAccount = getAccountById(creditAccountId);
        var fiTechBlock = getFiTechBlock(orderId, debitAccount.getId());

        debitAccount.debit(amount, fiTechBlock);
        creditAccount.credit(amount);
        fiTechBlockRepository.delete(fiTechBlock);
    }

    private FiTechBlock getFiTechBlock(Long orderId, Long accountId) {
        return fiTechBlockRepository.findByOrderIdAndAccountId(orderId, accountId)
                .orElseThrow(() -> new FiNotBlockedException(orderId));
    }

    private Account getAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findByIdWithLock(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
