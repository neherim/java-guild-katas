package com.github.neherim.guild.example.entity;

import com.github.neherim.guild.example.utils.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity<Long> {
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private Integer balance;
}
