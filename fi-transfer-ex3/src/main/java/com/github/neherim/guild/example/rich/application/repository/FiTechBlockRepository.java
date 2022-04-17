package com.github.neherim.guild.example.rich.application.repository;

import com.github.neherim.guild.example.rich.domain.FiTechBlock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface FiTechBlockRepository extends CrudRepository<FiTechBlock, Long> {
    Optional<FiTechBlock> findByOrderIdAndAccountId(Long orderId, Long accountId);
}
