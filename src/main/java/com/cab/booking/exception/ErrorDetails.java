package com.cab.booking.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {

	private String error;
	private String details;
	private LocalDateTime timeStamp;
}
