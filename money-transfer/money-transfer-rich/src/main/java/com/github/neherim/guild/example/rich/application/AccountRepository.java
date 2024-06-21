package com.github.neherim.guild.example.rich.application;

import com.github.neherim.guild.example.rich.domain.Account;
import com.github.neherim.guild.example.rich.domain.AccountId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AccountRepository extends Repository<Account, AccountId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findByIdWithLock(AccountId id);

    Account save(Account entity);

    Optional<Account> findById(AccountId id);
}
