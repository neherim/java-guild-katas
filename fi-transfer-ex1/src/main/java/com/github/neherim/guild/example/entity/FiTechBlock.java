package com.github.neherim.guild.example.entity;


import lombok.*;

import javax.persistence.*;

/**
 * Запись о технической блокировке ценных бумаг
 */
@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "t_account_fi_tech_block")
public class FiTechBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dps_seq")
    @SequenceGenerator(name = "dps_seq", sequenceName = "dps_seq", allocationSize = 1)
    private Long id;
    private Long orderId;
    private Long accountId;
    private Integer amount;
}
