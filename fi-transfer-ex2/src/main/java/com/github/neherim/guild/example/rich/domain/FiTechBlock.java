package com.github.neherim.guild.example.rich.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Запись о технической блокировке ценных бумаг
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_account_fi_tech_block")
public class FiTechBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dps_seq")
    @SequenceGenerator(name = "dps_seq", sequenceName = "dps_seq", allocationSize = 1)
    private Long id;
    private Long orderId;
    private Integer amount;

    /**
     * Создает новую техническую блокировку ц/б
     *
     * @param orderId идентификатор поручения по которому накладываем блокировку
     * @param amount  количество заблокированных ц/б
     */
    public FiTechBlock(Long orderId, Integer amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
