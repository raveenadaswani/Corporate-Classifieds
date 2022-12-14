package com.cts.authmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.authmicroservice.exception.UnauthorizedException;
import com.cts.authmicroservice.model.AuthResponse;
import com.cts.authmicroservice.model.UserModel;
import com.cts.authmicroservice.model.UserToken;
import com.cts.authmicroservice.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	UserServiceImpl userServiceImpl;

	/**
	 * authenticates the user
	 */
	@PostMapping("/login")
	public ResponseEntity<UserToken> login(@RequestBody UserModel user) {
		log.info("Inside Login : ");
		return new ResponseEntity<UserToken>(userServiceImpl.login(user), HttpStatus.OK);
	}
	/**
	 */
	@GetMapping("/validate")
	public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization") String token) {
		log.info("Inside Token Validation... ");
		return new ResponseEntity<AuthResponse>(userServiceImpl.getValidity(token), HttpStatus.OK);
	}


}
