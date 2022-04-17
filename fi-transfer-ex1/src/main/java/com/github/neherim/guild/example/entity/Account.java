package com.github.neherim.guild.example.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
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
}
