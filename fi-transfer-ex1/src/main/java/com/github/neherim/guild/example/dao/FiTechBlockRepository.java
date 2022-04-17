package com.github.neherim.guild.example.dao;

import com.github.neherim.guild.example.entity.FiTechBlock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FiTechBlockRepository extends CrudRepository<FiTechBlock, Long> {

    Optional<FiTechBlock> findByOrderIdAndAccountId(Long orderId, Long accountId);

    @Query("select coalesce(sum(b.amount), 0) from FiTechBlock b where b.accountId = :accountId")
    Integer getTotalBlockedAmount(Long accountId);
}
