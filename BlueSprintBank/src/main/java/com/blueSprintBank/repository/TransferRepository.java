package com.blueSprintBank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blueSprintBank.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long>{
	

}
