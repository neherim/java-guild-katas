package com.github.neherim.guild.example.rich.domain;


import com.github.neherim.guild.example.rich.utils.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoneyReservation extends BaseEntity<Long> {
    private Long operationId;
    private Integer amount;
    @ManyToOne
    @JoinColumn(name = "link_account", nullable = false)
    private Account account;
}
