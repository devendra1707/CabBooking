package com.cab.booking.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Notification {

	private String nearestDriver;
	private String message;

	private LocalDateTime timestamp;

	private String type;

	private Driver driver;

	private Ride ride;
}
