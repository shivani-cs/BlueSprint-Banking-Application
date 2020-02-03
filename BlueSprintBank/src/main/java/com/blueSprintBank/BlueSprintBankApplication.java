package com.blueSprintBank;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlueSprintBankApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Pst/UTC"));
		SpringApplication.run(BlueSprintBankApplication.class, args);
	}

}
