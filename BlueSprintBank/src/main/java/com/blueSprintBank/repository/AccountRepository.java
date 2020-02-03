package com.blueSprintBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blueSprintBank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
