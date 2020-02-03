package com.blueSprintBank.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Transfer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	private long userId;
	
	private String fromType;
	
	private String toType;
	
	private double amount;
	
	private boolean isRecurring;
	
	private LocalDate date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getToType() {
		return toType;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate localdate) {
		this.date = localdate;
	}


	
}
