package com.blueSprintBank.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blueSprintBank.entity.Payment;
import com.blueSprintBank.repository.PaymentRepository;

@Service
public class PaymentRepositoryService {

	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	EntityManager entityManager;
	
	public Payment pay(Payment payment) {
		Payment payment1 = paymentRepository.save(payment);
		System.out.println(
				"\n - - - - - - - - - - " + payment1.getAmount() + " transferred successfully! - - - - - - - - - - -\n");
		return payment1;
	}
	
	public List<Payment> getRecurring(long userId) {
		Query query = entityManager.createQuery("SELECT t FROM Payment t WHERE t.fromUser = :userId AND t.isRecurring=true");
		query.setParameter("userId", userId);
		System.out.println("Before Query:"+query);
		List<Payment> payments = new ArrayList<>();
		try {
			payments = query.getResultList();
		} catch(Exception e) {
			System.out.println("Error in Getting Transactions: "+ e);	
		}
		return payments;
	}
}
