package com.github.neherim.guild.example.rich.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class EntityId<T> {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "seq", allocationSize = 1)
    private T id;

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
