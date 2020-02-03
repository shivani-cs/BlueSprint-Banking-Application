

package com.blueSprintBank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blueSprintBank.entity.User;
import com.blueSprintBank.service.UserRepositoryService;


@RestController
public class UserController {

	@Autowired
	private UserRepositoryService userService;


	@PostMapping("/signup")
	
	public @ResponseBody String addNewUser(@RequestBody User user) {
		System.out.println("User in addNewUser" + user.getEmail());

		String email = user.getEmail();
		
		User temp = userService.findUserByEmail(email);
		if(temp == null)
			userService.createUser(user);
		else
			return "User is already registered";
		return "Saved";
	}
	
	@GetMapping("/ping")
	public @ResponseBody String ping() {
		return "Spring Boot API Version 2.0 is alive!";
	}

}
