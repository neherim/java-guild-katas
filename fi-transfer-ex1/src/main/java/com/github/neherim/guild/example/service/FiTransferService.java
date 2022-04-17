package com.github.neherim.guild.example.service;

import com.github.neherim.guild.example.dao.AccountRepository;
import com.github.neherim.guild.example.dao.FiTechBlockRepository;
import com.github.neherim.guild.example.entity.Account;
import com.github.neherim.guild.example.entity.FiTechBlock;
import com.github.neherim.guild.example.exceptions.AccountNotFoundException;
import com.github.neherim.guild.example.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.exceptions.FiNotEnoughException;
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
     * @throws AccountNotFoundException  счет не найден
     * @throws FiNotEnoughException      недостаточно ц.б. для проведения технической блокировки
     * @throws FiAlreadyBlockedException бумаги уже были заблокированы по данному поручению
     */
    @Transactional
    public void makeTechBlock(Long orderId, Long accountId, Integer amount)
            throws AccountNotFoundException, FiNotEnoughException, FiAlreadyBlockedException {
        // Ищем счет
        var account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        // Проверяем, нет ли уже блокировки по этому поручению
        var fiTechBlockOpt = fiTechBlockRepository.findByOrderIdAndAccountId(orderId, accountId);
        if (fiTechBlockOpt.isPresent()) {
            throw new FiAlreadyBlockedException(account, orderId);
        }

        // Проверяем, хватает ли незаблокированных ц.б. для новой блокировки
        var totalBlockedAmount = fiTechBlockRepository.getTotalBlockedAmount(accountId);
        if (amount > account.getBalance() - totalBlockedAmount) {
            throw new FiNotEnoughException(account, amount);
        }

        // Сохраняем новую блокировку
        fiTechBlockRepository.save(new FiTechBlock(null, orderId, accountId, amount));
    }

    /**
     * Второй этап. Снятие блокировки и перевод ценных бумаг
     *
     * @param orderId         id поручения на перевод бумаг
     * @param debitAccountId  id счета с которого списываем
     * @param creditAccountId id счета на который зачисляем
     * @param amount          количество бумаг
     * @throws AccountNotFoundException не найден счет списания или зачисления
     * @throws FiNotBlockedException    бумаги не были предварительно заблокированы
     */
    @Transactional
    public void transferFi(Long orderId, Long debitAccountId, Long creditAccountId, Integer amount)
            throws AccountNotFoundException, FiNotBlockedException {
        if (debitAccountId.equals(creditAccountId)) {
            return;
        }

        // Получаем счета
        var debitAccount = getAccountById(debitAccountId);
        var creditAccount = getAccountById(creditAccountId);

        // Ищем блокировку бумаг по orderId поручения
        var fiTechBlock = fiTechBlockRepository.findByOrderIdAndAccountId(orderId, debitAccountId)
                .orElseThrow(() -> new FiNotBlockedException(orderId));

        // Убираем блокировку бумаг
        fiTechBlockRepository.delete(fiTechBlock);
        // Списываем бумаги
        debitAccount.setBalance(debitAccount.getBalance() - amount);
        // Зачисляем бумаги
        creditAccount.setBalance(creditAccount.getBalance() + amount);
    }

    private Account getAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}
