package com.cab.booking.service;

import com.cab.booking.exception.DriverException;
import com.cab.booking.exception.RideException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.request.RideRequest;

public interface RideService {

	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;

	public Ride createRideRequest(User user, Driver nearsDriver, double pinkupLatitude, double pickupLongitude,
			double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea);

	public void acceptRide(Integer rideId) throws RideException;

	public void declineRide(Integer rideId, Integer driverId) throws RideException;

	public void startRide(Integer rideId, int opt) throws RideException;

	public void completeRide(Integer rideId) throws RideException;

	public void cancleRide(Integer rideId) throws RideException;

	public Ride findRideById(Integer rideId) throws RideException;

}
