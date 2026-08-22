package com.fundoonotes.exception;

public class InvalidLoginException extends RuntimeException {

	public InvalidLoginException(String message) {
		super(message);
	}
}
