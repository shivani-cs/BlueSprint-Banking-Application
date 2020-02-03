package com.blueSprintBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blueSprintBank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
