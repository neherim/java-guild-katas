package com.github.neherim.guild.example.rich.domain;

import com.github.neherim.guild.example.rich.domain.exceptions.FiAlreadyBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotBlockedException;
import com.github.neherim.guild.example.rich.domain.exceptions.FiNotEnoughException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dps_seq")
    @SequenceGenerator(name = "dps_seq", sequenceName = "dps_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private Integer balance; // Количество ценных бумаг на счете

    // Список технических блокировок бумаг на этом счете для последующих операций списания
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "link_account", nullable = false, updatable = false)
    private Set<FiTechBlock> blockedFi;

    /**
     * Открыть новый счет
     *
     * @param openDateTime дата/время открытия счтеа
     * @return новый лицевой счет
     */
    public static Account openAccount(LocalDateTime openDateTime) {
        return new Account(null, openDateTime, null, 0, new HashSet<>());
    }

    /**
     * Списываем ц.б. со счета
     *
     * @param amount количество ц.б.
     */
    public void debit(Long orderId, Integer amount) throws FiNotBlockedException {
        if (removeTechBlock(orderId)) {
            balance = balance - amount;
        } else {
            throw new FiNotBlockedException(orderId);
        }
    }

    /**
     * Зачисляем ц.б. на счет
     *
     * @param amount количество ц.б.
     */
    public void credit(Integer amount) {
        balance = balance + amount;
    }

    /**
     * Провести техническую блокировку
     *
     * @param orderId поручение на основании которого блокируем бумаги
     * @param amount  количество бумаг для блокирования
     * @throws FiNotEnoughException недостаточно ц.б. для проведения технической блокировки
     */
    public void makeTechBlock(Long orderId, Integer amount) throws FiNotEnoughException, FiAlreadyBlockedException {
        if (findFiTechBlockByOrderId(orderId).isPresent()) {
            throw new FiAlreadyBlockedException(this, orderId);
        }
        if (amount > getNotBlockedBalance()) {
            throw new FiNotEnoughException(this, amount);
        }
        blockedFi.add(new FiTechBlock(orderId, amount));
    }

    /**
     * Удалить техническую блокировку, связанную с поручением
     *
     * @param orderId поручение на основании которого была проведена техническая блокировка
     * @return true в случае если блокировка удалена, false если такой блокировки не было
     */
    public boolean removeTechBlock(Long orderId) {
        var blockOpt = findFiTechBlockByOrderId(orderId);
        if (blockOpt.isPresent()) {
            blockedFi.remove(blockOpt.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Получить не заблокированный остаток
     *
     * @return Остаток бумаг, учитывая технические блокировки
     */
    public Integer getNotBlockedBalance() {
        return balance - getTechBlockedAmount();
    }

    /**
     * Получить реальный остаток
     *
     * @return Реальный остаток бумаг, не учитывая технические блокировки
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * Получить сумму всех заблокированных бумаг
     *
     * @return сумма всех заблокированных бумаг
     */
    private Integer getTechBlockedAmount() {
        return blockedFi.stream().mapToInt(FiTechBlock::getAmount).sum();
    }

    private Optional<FiTechBlock> findFiTechBlockByOrderId(Long orderId) {
        return blockedFi.stream().filter(blk -> blk.getOrderId().equals(orderId)).findAny();
    }
}
