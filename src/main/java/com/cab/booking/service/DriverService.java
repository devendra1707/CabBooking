package com.cab.booking.service;

import java.util.List;

import com.cab.booking.exception.DriverException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;
import com.cab.booking.request.DriversSignupRequest;

public interface DriverService {


	public Driver registerDriver(DriversSignupRequest driversSignupRequest);

	public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude,  Ride ride);

	public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);

	public Driver getReqDriverProfile(String jwt) throws DriverException;

	public Ride getDriversCurrentRide(Integer driverId) throws DriverException;

	public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;

	public Driver findDriverById(Integer friverId) throws DriverException;

	public List<Ride> completedRides(Integer driverId) throws DriverException;

}
