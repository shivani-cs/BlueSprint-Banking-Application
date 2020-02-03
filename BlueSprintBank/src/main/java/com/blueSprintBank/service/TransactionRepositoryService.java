package com.blueSprintBank.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blueSprintBank.entity.Transaction;
import com.blueSprintBank.entity.User;
import com.blueSprintBank.repository.TransactionRepository;
import com.blueSprintBank.repository.UserRepository;


@Service
public class TransactionRepositoryService {

	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	EntityManager entityManager;

	public Transaction addTransaction(Transaction transaction) {
		transaction = transactionRepository.save(transaction);
		System.out.println(
				"\n - - - - - - - - - - User added " + transaction.getAmount() + " to his account successfully! - - - - - - - - - - -\n");
		return transaction;
	}
	
	public List<Transaction> searchTransaction(long userId, Date startDate, Date endDate) {
		Query query = entityManager.createQuery("SELECT t FROM Transaction t WHERE (t.userId = :userId OR t.toId = :toId) AND t.date>= :startDate AND t.date<= :endDate");
		query.setParameter("userId", userId);
		query.setParameter("toId", userId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		System.out.println("Before Query:"+query);
		List<Transaction> transactions = new ArrayList<>();
		try {
			transactions = query.getResultList();
		} catch(Exception e) {
			System.out.println("Error in Getting Transactions: "+ e);	
		}
		
		return transactions;
	}
	
}
