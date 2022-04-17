package com.github.neherim.guild.example.rich.application.repository;

import com.github.neherim.guild.example.rich.domain.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.Optional;

@Component
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdWithLock(Long id);
}
