package com.fundoonotes.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
