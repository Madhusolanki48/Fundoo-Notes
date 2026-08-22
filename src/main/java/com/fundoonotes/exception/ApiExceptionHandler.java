package com.fundoonotes.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		return error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<Map<String, String>> handleInvalidLogin(InvalidLoginException ex) {
		return error(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NoteNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNoteNotFound(NoteNotFoundException ex) {
		return error(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidNoteStateException.class)
	public ResponseEntity<Map<String, String>> handleInvalidNoteState(InvalidNoteStateException ex) {
		return error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LabelAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleLabelAlreadyExists(LabelAlreadyExistsException ex) {
		return error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LabelNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleLabelNotFound(LabelNotFoundException ex) {
		return error(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Map<String, String>> error(String message, HttpStatus status) {
		Map<String, String> body = new HashMap<>();
		body.put("message", message);
		return new ResponseEntity<>(body, status);
	}
}
