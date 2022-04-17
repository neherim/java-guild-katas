package com.github.neherim.guild.example.rich.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Запись о технической блокировке ценных бумаг
 */
@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_account_fi_tech_block")
public class FiTechBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dps_seq")
    @SequenceGenerator(name = "dps_seq", sequenceName = "dps_seq", allocationSize = 1)
    private Long id;
    private Long orderId;

    @Column(name = "link_account")
    private Long accountId;
    private Integer amount;
}
