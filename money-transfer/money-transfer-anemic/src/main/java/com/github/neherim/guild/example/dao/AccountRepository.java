package com.github.neherim.guild.example.dao;

import com.github.neherim.guild.example.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AccountRepository extends CrudRepository<Account, Long> {

}
