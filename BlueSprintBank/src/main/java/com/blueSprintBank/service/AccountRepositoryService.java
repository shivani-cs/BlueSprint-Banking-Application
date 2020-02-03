package com.blueSprintBank.service;

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
import org.springframework.transaction.annotation.Transactional;

import com.blueSprintBank.entity.Account;
import com.blueSprintBank.repository.AccountRepository;



@Service
public class AccountRepositoryService {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	EntityManager entityManager;

	public Account addChecking(Account account) {
		Account account2 = accountRepository.save(account);
		System.out.println(
				"\n - - - - - - - - - - User " + account.getUserId() + " added checking account successfully! - - - - - - - - - - -\n");
		
		return account2;
	}
	
	public boolean hasChecking(long userId) {
		boolean flag = false;
		Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.userId = :userId AND a.type='Checking' AND a.isDeleted=false");
		query.setParameter("userId", userId);
		Account account = null;
		try {
			account = (Account) query.getSingleResult();
		} catch(NoResultException e) {
			
		}
		if (account!=null) flag=true;
		return flag;
	}
	
	public Account addSavings(Account account) {
		Account account2 = accountRepository.save(account);
		System.out.println(
				"\n - - - - - - - - - - User " + account.getUserId() + " added savings account successfully! - - - - - - - - - - -\n");
		
		return account2;
	}
	
	public boolean hasSavings(long userId) {
		boolean flag = false;
		Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.userId = :userId AND a.type='Savings' AND isDeleted=false");
		query.setParameter("userId", userId);
		Account account = null;
		try {
			account = (Account) query.getSingleResult();
		} catch(NoResultException e) {
			
		}
		if (account!=null) flag=true;
		return flag;
	}
	
	@Transactional
	public String deleteAccount(long userId, String type) {
		
		
		Query query = entityManager.createQuery("UPDATE Account a SET isDeleted= true WHERE a.userId = :userId AND a.type= :type AND isDeleted=false");
		query.setParameter("userId", userId);
		query.setParameter("type", type);
		String response = null;
		try {
			System.out.println("in delete account function");
			response =  String.valueOf(query.executeUpdate());
			System.out.println("response===== "+response);
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		System.out.println("response===== "+response);
		return response;
	}
	
	public Account findAccount(long userId, String type) {
		Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.userId = :userId AND a.type= :type AND isDeleted=false");
		query.setParameter("userId", userId);
		query.setParameter("type", type);
		Account account = null;
		try {
			account = (Account) query.getSingleResult();
		} catch(Exception e) {
			System.out.println("Error in getting account details");
		}
		return account;
	}
	
	@Transactional
	public String updateCheckingBalance(long userId, long toId, double balance) {
		Account fromAccount = new Account();
		Account toAccount = new Account();
		if (hasChecking(userId) && hasChecking(toId))  {
			fromAccount = findAccount(userId, "Checking");
			toAccount = findAccount(toId, "Checking");
		}	
		else 
			return "No Checking account";
		
		double updatedBalancefrom = fromAccount.getBalance() - balance;
		Query query = entityManager.createQuery("UPDATE Account a SET balance = :updatedBalancefrom WHERE a.userId = :userId AND a.type = :type");
		query.setParameter("userId", userId);
		query.setParameter("type", "Checking");
		query.setParameter("updatedBalancefrom", updatedBalancefrom);
		
		double updatedBalanceto = toAccount.getBalance() + balance;
		Query query2 = entityManager.createQuery("UPDATE Account a SET balance = :updatedBalanceto WHERE a.userId = :userId AND a.type = :type");
		query2.setParameter("userId", toId);
		query2.setParameter("type", "Checking");
		query2.setParameter("updatedBalanceto", updatedBalanceto);

		String response = null;
		try {
			System.out.println("in update balance function");
			response =  String.valueOf(query.executeUpdate());
			response +=  String.valueOf(query2.executeUpdate());
			System.out.println("response===== "+response);
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		System.out.println("response===== "+response);
		
		return response;
	}
	
	@Transactional
	public String updateBalance(long userId, String fromType, String toType, double balance) {
		
		Account fromAccount = findAccount(userId, fromType);
		double updatedBalancefrom = fromAccount.getBalance() - balance;
		Query query = entityManager.createQuery("UPDATE Account a SET balance = :updatedBalancefrom WHERE a.userId = :userId AND a.type = :type");
		query.setParameter("userId", userId);
		query.setParameter("type", fromType);
		query.setParameter("updatedBalancefrom", updatedBalancefrom);
		
		Account toAccount = findAccount(userId, toType);
		double updatedBalanceto = toAccount.getBalance() + balance;
		Query query2 = entityManager.createQuery("UPDATE Account a SET balance = :updatedBalanceto WHERE a.userId = :userId AND a.type = :type");
		query2.setParameter("userId", userId);
		query2.setParameter("type", toType);
		query2.setParameter("updatedBalanceto", updatedBalanceto);
		
		String response = null;
		try {
			System.out.println("in update balance function");
			response =  String.valueOf(query.executeUpdate());
			response +=  String.valueOf(query2.executeUpdate());
			System.out.println("response===== "+response);
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		System.out.println("response===== "+response);
		
		return response;
	}
	
	@Transactional
	public String refund(long userId, String fromType, double balance) {
		Account fromAccount = new Account();
		if (hasChecking(userId) || hasSavings(userId))  {
			fromAccount = findAccount(userId, fromType);
		}	
		else 
			return "No account";
		
		double updatedBalancefrom = fromAccount.getBalance() + balance;
		Query query = entityManager.createQuery("UPDATE Account a SET balance = :updatedBalancefrom WHERE a.userId = :userId AND a.type = :type");
		query.setParameter("userId", userId);
		query.setParameter("type", fromType);
		query.setParameter("updatedBalancefrom", updatedBalancefrom);
		
		String response = null;
		try {
			System.out.println("in refund function");
			response =  String.valueOf(query.executeUpdate());
			System.out.println("response===== "+response);
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		System.out.println("response===== "+response);
		
		return response;
	}
		
	public Account updateAccount(Account account) {
		Account account2 = accountRepository.save(account);
		System.out.println(
				"\n - - - - - - - - - - User " + account.getUserId() + " updated account successfully! - - - - - - - - - - -\n");
		
		return account2;
	}
	
}