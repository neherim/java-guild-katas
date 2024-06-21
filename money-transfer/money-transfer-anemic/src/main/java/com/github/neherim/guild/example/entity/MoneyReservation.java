package com.github.neherim.guild.example.entity;


import com.github.neherim.guild.example.utils.BaseEntity;
import lombok.*;

import jakarta.persistence.Entity;

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
