package com.blueSprintBank.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blueSprintBank.entity.Transfer;
import com.blueSprintBank.repository.TransferRepository;

@Service
public class TransferRepositoryService {

	@Autowired
	TransferRepository transferRepository;
	
	@Autowired
	EntityManager entityManager;
	
	public Transfer transferAmount(Transfer transfer) {
		Transfer transfer1 = transferRepository.save(transfer);
		System.out.println(
				"\n - - - - - - - - - - " + transfer1.getAmount() + " transferred successfully! - - - - - - - - - - -\n");
		return transfer1;
	}
	
	public List<Transfer> getRecurring(long userId) {
		Query query = entityManager.createQuery("SELECT t FROM Transfer t WHERE t.userId = :userId AND t.isRecurring = true ");
		query.setParameter("userId", userId);
		System.out.println("Before Query:"+query);
		List<Transfer> transfers = null;
		try {
			transfers = query.getResultList();
		} catch(Exception e) {
			System.out.println("Error in Getting Transactions: "+ e);	
		}
		return transfers;
	}
}
