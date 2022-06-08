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
     */
    public void debit(Long orderId) {
        var block = removeTechBlock(orderId).orElseThrow(() -> new FiNotBlockedException(orderId));
        balance = balance - block.getAmount();
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
     */
    public void makeTechBlock(Long orderId, Integer amount) {
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
    public Optional<FiTechBlock> removeTechBlock(Long orderId) {
        var blockOpt = findFiTechBlockByOrderId(orderId);
        blockOpt.ifPresent(blk -> blockedFi.remove(blk));
        return blockOpt;
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
