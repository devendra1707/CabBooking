package com.cab.booking.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class Calculators {

	private static final int EARTH_RADIUS = 6371;

	public double calculateDistance(double sourceLatitude, double sourceLongitude, double descLatitude,
			double descLongitude) {

		double dLat = Math.toRadians(descLatitude - sourceLatitude);
		double dLng = Math.toRadians(descLongitude - sourceLongitude);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(sourceLatitude))
				* Math.cos(Math.toRadians(dLat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = EARTH_RADIUS * c;

		return distance;
	}

	public long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
		Duration duration = Duration.between(startTime, endTime);
		return duration.getSeconds();
	}

	public double calculateFare(double distance) {
		double baseFare = 11;
		double totalFare = baseFare * distance;
		return totalFare;
	}
}
