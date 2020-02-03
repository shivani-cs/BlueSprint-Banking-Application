
package com.blueSprintBank.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blueSprintBank.entity.Account;
import com.blueSprintBank.entity.Transaction;
import com.blueSprintBank.service.AccountRepositoryService;
import com.blueSprintBank.service.TransactionRepositoryService;
import com.blueSprintBank.service.UserRepositoryService;

@RestController
public class AccountController {

	@Autowired
	private UserRepositoryService userService;

	@Autowired
	private AccountRepositoryService accountService;

	@Autowired
	private TransactionRepositoryService transactionService;

	@PostMapping("/addChecking")

	public @ResponseBody Map<Object, Object> addCheckingAccount(@RequestBody HashMap<String, String> map) {

		Map<Object, Object> responseBody = new HashMap<>();
		// Get all properties
		long userId = new Long(map.get("userId"));
		double amount = new Double(map.get("amount"));

		Account account = new Account();
		// Set all Properties
		account.setUserId(userId);
		account.setBalance(amount);
		account.setType("Checking");

		System.out.println("User is creating checking account" + account.getUserId());

		if (!accountService.hasChecking(userId)) {
			accountService.addChecking(account);
			Transaction transaction = new Transaction();
			transaction.setUserId(userId);
			transaction.setAmount(amount);
			transaction.setFromType("checking");
			transaction.setTransactionType("account opened");
			transaction.setDate(new Date());
			responseBody.put("message", "Checking Account Created Successfully");
		} else {
			responseBody.put("message", "Checking Account Already Exists");
		}

	
		return responseBody;
	}

	@PostMapping("/addSavings")

	public @ResponseBody Map<Object, Object> addSavingsAccount(@RequestBody HashMap<String, String> map) {

		Map<Object, Object> responseBody = new HashMap<>();
		// Get all properties
		long userId = new Long(map.get("userId"));
		double amount = new Double(map.get("amount"));

		Account account = new Account();
		// Set all Properties
		account.setUserId(userId);
		account.setBalance(amount);
		account.setType("Savings");

		System.out.println("User is creating savings account" + account.getUserId());

		if (!accountService.hasSavings(userId)) {
			accountService.addSavings(account);
			Transaction transaction = new Transaction();
			transaction.setUserId(userId);
			transaction.setAmount(amount);
			transaction.setFromType("savings");
			transaction.setTransactionType("account opened");
			transaction.setDate(new Date());
			responseBody.put("message", "Savings Account Created Successfully");
		} else {
			responseBody.put("message", "Savings Account Already Exists");
		}

		return responseBody;
	}

	@PutMapping("/closeAccount")
	public @ResponseBody Map<Object, Object> closeAccount(@RequestBody HashMap<String, String> map) {

		Map<Object, Object> responseBody = new HashMap<>();
		// Get all properties
		long userId = new Long(map.get("userId"));
		String type = (String) map.get("type");
		Account account = accountService.findAccount(userId, type);
		if (account != null) {

			Transaction transaction = new Transaction();
			transaction.setUserId(userId);
			transaction.setAmount(0 - account.getBalance());
			transaction.setFromType(account.getType());
			transaction.setTransactionType("account closed");
			transaction.setDate(new Date());
			account.setBalance(0);
			account.setDeleted(true);
			accountService.updateAccount(account);
			transactionService.addTransaction(transaction);
			responseBody.put("message", type + " Account deleted successfully and balance is given to the user");
		} else {
			responseBody.put("message", type + " Account was already deleted");
		}

		return responseBody;
	}

	@GetMapping("/world")
	public String test() throws ParseException {
		return "Spring Boot Application is running";
	}

}
