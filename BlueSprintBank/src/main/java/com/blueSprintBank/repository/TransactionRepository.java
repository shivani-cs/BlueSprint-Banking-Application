package com.blueSprintBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blueSprintBank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	

}
