package com.github.neherim.guild.example.dao;

import com.github.neherim.guild.example.entity.MoneyReservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MoneyReservationRepository extends CrudRepository<MoneyReservation, Long> {

    Optional<MoneyReservation> findByOperationIdAndAccountId(Long operationId, Long accountId);

    @Query("select coalesce(sum(b.amount), 0) from MoneyReservation b where b.accountId = :accountId")
    Integer getTotalReservedAmount(Long accountId);
}
