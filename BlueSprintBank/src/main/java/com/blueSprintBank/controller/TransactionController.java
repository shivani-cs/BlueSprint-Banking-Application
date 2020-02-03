
package com.blueSprintBank.controller;

import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blueSprintBank.entity.Transaction;
import com.blueSprintBank.entity.User;
import com.blueSprintBank.service.AccountRepositoryService;
import com.blueSprintBank.service.TransactionRepositoryService;
import com.blueSprintBank.service.UserRepositoryService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionRepositoryService transactionService;
	
	@Autowired
	private AccountRepositoryService accountService;

	@GetMapping("/searchTransactions")

	public @ResponseBody Map<Object, Object> searchTransactions(@RequestParam(name = "startDate") String startDate,
			@RequestParam(name = "endDate") String endDate, @RequestParam(name = "userId") String userId,
			HttpServletResponse response) throws ParseException {
		String start = startDate.concat("T00:00:00.0000000Z");
		String end = endDate.concat("T23:59:59.0000000Z");
		long user_id = new Long(userId);
		Date start_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse(start);
		Date end_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'").parse(end);

		Map<Object, Object> responseBody = new HashMap<>();
		try {
			List<Transaction> transactions = transactionService.searchTransaction(user_id, start_date, end_date);
			List<Map<Object, Object>> transactionDetails = new ArrayList<Map<Object, Object>>();
			for (Transaction transaction : transactions) {
				System.out.println(transaction.getDate());
				transactionDetails.add(formTransaction(transaction));
			}
			System.out.println("transaction Details::::::"+ transactionDetails);
			responseBody.put("Transaction Details", transactionDetails);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseBody.put("error", e.getMessage());
		}
		return responseBody;
	}

	public Map<Object, Object> formTransaction(Transaction transaction) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("Date", transaction.getDate());
		map.put("From User", transaction.getUserId());
		map.put("To User", transaction.getToId());
		map.put("From Account Type", transaction.getFromType());
		map.put("To Account Type", transaction.getToType());
		map.put("Amount", transaction.getAmount());
		map.put("Transaction Type", transaction.getTransactionType());
		return map;
	}
	
	@PostMapping("/addManualTransaction")
	public @ResponseBody Map<Object, Object> addManualTransaction(@RequestBody Map<String, String> map){
		Map<Object, Object> responseBody = new HashMap<>();
		
		try {
			
			long userId = new Long(map.get("userId"));
			String fromType = new String(map.get("fromType"));
			// long toID = new Long(map.get("toUser"));
			double amount = new Double(map.get("amount"));
			//boolean recurring = new Boolean(map.get("isRecurring"));
			
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setUserId(userId);
			transaction.setDate(new Date());
			//transaction.setToType("Checking");
			transaction.setFromType(fromType);
			transaction.setTransactionType("Refund");
			accountService.refund(userId, fromType, amount);
			transactionService.addTransaction(transaction);
			
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		
		responseBody.put("message", "Transaction added Successfully");
		return responseBody;
	}
}
