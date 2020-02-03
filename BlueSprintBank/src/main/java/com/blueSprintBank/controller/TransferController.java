package com.blueSprintBank.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blueSprintBank.service.AccountRepositoryService;
import com.blueSprintBank.service.PaymentRepositoryService;
import com.blueSprintBank.service.TransactionRepositoryService;
import com.blueSprintBank.service.TransferRepositoryService;



import com.blueSprintBank.entity.Payment;
import com.blueSprintBank.entity.Transaction;
import com.blueSprintBank.entity.Transfer;

@RestController
public class TransferController {

	@Autowired
	private TransferRepositoryService transferService;
	
	@Autowired
	private AccountRepositoryService accountService;
	
	@Autowired
	private TransactionRepositoryService transactionService;
	
	@Autowired
	private PaymentRepositoryService paymentService;
	
	@PostMapping("/transferBetweenAccounts")
	
	public @ResponseBody Map<Object, Object> transferBetweenAccount(@RequestBody Map<String, String> map) {
		
		Map<Object, Object> responseBody = new HashMap<>();
		try {
			long ID = new Long(map.get("userId"));
			System.out.println("User:" + map.get("userId"));
			String fromType = (String)map.get("fromType");
			String toType = (String) map.get("toType");
			double amount = new Double(map.get("amount"));
			boolean recurring = new Boolean(map.get("isRecurring"));
			
			Transfer transfer = new Transfer();
			transfer.setAmount(amount);
			transfer.setUserId(ID);
			transfer.setFromType(fromType);
			transfer.setToType(toType);
			transfer.setRecurring(recurring);
			LocalDate localdate = LocalDate.now();
			transfer.setDate(localdate);
			System.out.println("trying to transfer: " + amount + "from" + fromType + " to" + toType);
			transferService.transferAmount(transfer);
			accountService.updateBalance(ID, fromType, toType, amount);
			
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setUserId(ID);
			transaction.setDate(new Date());
			transaction.setToType(toType);
			transaction.setFromType(fromType);
			transaction.setTransactionType("between accounts");
			transactionService.addTransaction(transaction); 
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		
		responseBody.put("message", "Transferred Successfully");
		return responseBody;
	}	
	
	@PostMapping("/transferExternalPayee")
	public @ResponseBody Map<Object, Object> transferExternalPayee(@RequestBody Map<String, String> map) {
		
		Map<Object, Object> responseBody = new HashMap<>();
		
		try {
			long userId = new Long(map.get("userId"));
			long toID = new Long(map.get("toUser"));
			double amount = new Double(map.get("amount"));
			boolean recurring = new Boolean(map.get("isRecurring"));
		
			Payment payment = new Payment();
			payment.setAmount(amount);
			payment.setDate(new Date());
			payment.setFromUser(userId);
			payment.setToUser(toID);
			payment.setRecurring(recurring);
			System.out.println("trying to transfer: " + amount + "from" + userId + " to" + toID);
			paymentService.pay(payment);
			accountService.updateCheckingBalance(userId, toID, amount);
		
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setUserId(userId);
			transaction.setDate(new Date());
			transaction.setToType("Checking");
			transaction.setFromType("Checking");
			transaction.setTransactionType("external payee");
			transactionService.addTransaction(transaction);
		}catch(NoResultException e) {
			System.out.println("error"+e);
		}
		
		responseBody.put("message", "Transferred Successfully");
		return responseBody;
	}
	
	@GetMapping("/getRecurringBetweenAccounts/{userId}")
	public @ResponseBody Map<Object, Object> getRecurringBetweenAccounts(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "userId") long userId)  {
		
		

		Map<Object, Object> responseBody = new HashMap<>();
		try {
			List<Transfer> transfers = transferService.getRecurring(userId);
			List<Map<Object, Object>> transferDetails = new ArrayList<Map<Object, Object>>();
			for (Transfer transfer : transfers) {
			
				transferDetails.add(formTransfer(transfer));
			}
			System.out.println("transaction Details::::::"+ transferDetails);
			responseBody.put("Transaction Details", transferDetails);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseBody.put("error", e.getMessage());
		}
		return responseBody;
	}
	
	
	public Map<Object, Object> formTransfer(Transfer transfer) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("User", transfer.getUserId());
		map.put("From", transfer.getFromType());
		map.put("To", transfer.getToType());
		map.put("Amount", transfer.getAmount());
		map.put("Every month on this date", transfer.getDate());
		
		return map;
	}
	
	@GetMapping("/getRecurringPayments/{userId}")

	public @ResponseBody Map<Object, Object> getRecurringPayments(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "userId") long userId)  {
		
		

		Map<Object, Object> responseBody = new HashMap<>();
		try {
			List<Payment> payments = paymentService.getRecurring(userId);
			List<Map<Object, Object>> paymentDetails = new ArrayList<Map<Object, Object>>();
			for (Payment payment : payments) {
				
				paymentDetails.add(formPayment(payment));
			}
			System.out.println("payment Details::::::"+ paymentDetails);
			responseBody.put("Recurring Payment Details", paymentDetails);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseBody.put("error", e.getMessage());
		}
		return responseBody;
	}
	
	public Map<Object, Object> formPayment(Payment payment) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("From", payment.getFromUser());
		map.put("To", payment.getToUser());
		map.put("Amount", payment.getAmount());
		map.put("Every month on this date", payment.getDate());
		
		return map;
	}
}
