package com.github.neherim.guild.example.entity;


import com.github.neherim.guild.example.utils.BaseEntity;
import lombok.*;

import javax.persistence.Entity;

/**
 * Запись о технической блокировке ценных бумаг
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoneyReservation extends BaseEntity<Long> {
    private Long operationId;
    private Long accountId;
    private Integer amount;
}
