package com.fundoonotes.controller;

import com.fundoonotes.dto.AuthResponse;
import com.fundoonotes.dto.LoginRequest;
import com.fundoonotes.dto.RegisterRequest;
import com.fundoonotes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/userSignUp")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse response = userService.register(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = userService.login(request);
		return ResponseEntity.ok(response);
	}
}
