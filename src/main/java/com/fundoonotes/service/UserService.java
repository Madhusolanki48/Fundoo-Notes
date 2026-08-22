package com.fundoonotes.service;

import com.fundoonotes.dto.AuthResponse;
import com.fundoonotes.dto.LoginRequest;
import com.fundoonotes.dto.RegisterRequest;
import com.fundoonotes.entity.User;
import com.fundoonotes.exception.EmailAlreadyExistsException;
import com.fundoonotes.exception.InvalidLoginException;
import com.fundoonotes.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered");
		}

		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		userRepository.save(user);

		String token = jwtService.generateToken(user.getEmail());
		return new AuthResponse("User registered successfully", token);
	}

	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidLoginException("Invalid email or password"));

		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

		if (!passwordMatches) {
			throw new InvalidLoginException("Invalid email or password");
		}

		String token = jwtService.generateToken(user.getEmail());
		return new AuthResponse("Login successful", token);
	}
}
