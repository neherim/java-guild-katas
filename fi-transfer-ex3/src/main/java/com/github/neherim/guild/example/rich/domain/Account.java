package com.github.neherim.guild.example.rich.domain;

import com.github.neherim.guild.example.rich.domain.exceptions.FiNotEnoughException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Integer blockedBalance; // Количество заблокированных ценных бумаг на счете

    /**
     * Открыть новый счет
     *
     * @param openDateTime дата/время открытия счтеа
     * @return новый лицевой счет
     */
    public static Account openAccount(LocalDateTime openDateTime) {
        return new Account(null, openDateTime, null, 0, 0);
    }

    /**
     * Списываем ц.б. со счета
     *
     * @param amount      количество ц.б.
     * @param fiTechBlock запись о блокировки бумаг для списания
     */
    public void debit(Integer amount, FiTechBlock fiTechBlock) {
        // Проверяем, что нам передали правильную запись о блокировке
        if (id.equals(fiTechBlock.getAccountId()) && amount.equals(fiTechBlock.getAmount())) {
            blockedBalance = blockedBalance - amount;
            balance = balance - amount;
        } else {
            throw new IllegalArgumentException();
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
    public FiTechBlock makeTechBlock(Long orderId, Integer amount) throws FiNotEnoughException {
        if (amount > getNotBlockedBalance()) {
            throw new FiNotEnoughException(this, amount);
        }
        blockedBalance += amount;
        return new FiTechBlock(null, orderId, id, amount);
    }

    /**
     * Удалить техническую блокировку бумаг
     *
     * @param amount количество освобождаемых бумаг
     */
    public void removeFiBlock(Integer amount) {
        blockedBalance -= amount;
    }

    /**
     * Получить не заблокированный остаток
     *
     * @return Остаток бумаг, учитывая технические блокировки
     */
    public Integer getNotBlockedBalance() {
        return balance - blockedBalance;
    }

    /**
     * Получить реальный остаток
     *
     * @return Реальный остаток бумаг, не учитывая технические блокировки
     */
    public Integer getBalance() {
        return balance;
    }
}
