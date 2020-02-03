package com.blueSprintBank.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blueSprintBank.entity.Payment;
import com.blueSprintBank.entity.Transfer;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
