package com.cab.booking.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetails> userException(UserException userException, WebRequest webRequest) {

		ErrorDetails err = new ErrorDetails(userException.getMessage(), webRequest.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(DriverException.class)
	public ResponseEntity<ErrorDetails> driverExceptionHandler(DriverException de, WebRequest wr) {
		ErrorDetails err = new ErrorDetails(de.getMessage(), wr.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RideException.class)
	public ResponseEntity<ErrorDetails> rideExceptionHandler(RideException re, WebRequest wr) {
		ErrorDetails err = new ErrorDetails(re.getMessage(), wr.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDetails> handleValidationExceptionHandler(ConstraintViolationException ex) {

		StringBuilder errorMessage = new StringBuilder();

		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errorMessage.append(violation.getMessage() + "\n");

		}

		ErrorDetails err = new ErrorDetails(errorMessage.toString(), "Validation Error", LocalDateTime.now());
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumantNotValidExceptionHandler(MethodArgumentNotValidException manve) {
		ErrorDetails err = new ErrorDetails(manve.getBindingResult().getFieldError().getDefaultMessage(),
				"Validation error", LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> otherExceptionHandler(Exception de, WebRequest wr) {
		ErrorDetails err = new ErrorDetails(de.getMessage(), wr.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.ACCEPTED);
	}

}
