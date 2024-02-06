package com.cab.booking.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cab.booking.config.JwtUtil;
import com.cab.booking.domain.RideStatus;
import com.cab.booking.exception.DriverException;
import com.cab.booking.exception.RideException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.repo.DriverRepository;
import com.cab.booking.repo.LicenseReporitory;
import com.cab.booking.repo.NotificationRepository;
import com.cab.booking.repo.RideRepository;
import com.cab.booking.repo.VehicleRepository;
import com.cab.booking.request.RideRequest;
import com.cab.booking.service.DriverService;
import com.cab.booking.service.RideService;

@Service
public class RideServiceImpl implements RideService {

	@Autowired
	private DriverService driverService;

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private Calculators calculators;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private Calculators dictanceCalculator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private VehicleRepository vehicleRepository;
	@Autowired
	private LicenseReporitory licenseReporitory;

	@Override
	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {

		double pickupLatitude = rideRequest.getPickupLatitude();
		double pickupLongitude = rideRequest.getPickupLongitude();
		double distinationLatitude = rideRequest.getDistinationLatitude();
		double distinationLongitude = rideRequest.getDistinationLongitude();
		String pickupArea = rideRequest.getPickupArea();
		String destinationArea = rideRequest.getDestinationArea();

		Ride existingRide = new Ride();

		List<Driver> availableDrivers = driverService.getAvailableDrivers(pickupLatitude, pickupLongitude,
				existingRide);

		Driver nearestDriver = driverService.findNearestDriver(availableDrivers, pickupLatitude, pickupLongitude);

		if (nearestDriver == null) {
			throw new DriverException("Driver not availavle");
		}

		System.out.println("Duration ----- before ride");

		Ride ride = createRideRequest(user, nearestDriver, pickupLatitude, pickupLongitude, distinationLatitude,
				distinationLongitude, pickupArea, destinationArea);

		System.out.println("Duration ----- after ride");

		// Send a Notification to the driver
//		Notification notification = new Notification();
//		notification.setDriver(nearestDriver);
//		notification.setMessage("You have been allocated to a ride");
//		notification.setRide(ride);
//		notification.setTimestamp(LocalDateTime.now());
//		notification.setType(NotificationType.Ri);

		return ride;
	}

	@Override
	public Ride createRideRequest(User user, Driver nearsDriver, double pinkupLatitude, double pickupLongitude,
			double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea) {

		Ride ride = new Ride();

		ride.setDriver(nearsDriver);
		ride.setUser(user);
		ride.setPickupLatitude(pinkupLatitude);
		ride.setPickupLongitude(pickupLongitude);
//		ride.setDe
		ride.setDestinationLongitude(destinationLongitude);
		ride.setStatus(RideStatus.REQUESTED);
		ride.setPickupArea(pickupArea);
		ride.setDestinationArea(destinationArea);
		ride.setDestinationLatitude(destinationLatitude);

		System.out.println("----------a -" + pickupLongitude);

		return rideRepository.save(ride);
	}

	@Override
	public void acceptRide(Integer rideId) throws RideException {

		Ride ride = findRideById(rideId);
		ride.setStatus(RideStatus.ACCEPTED);
		Driver driver = ride.getDriver();
		driver.setCurrentRide(ride);
		Random random = new Random();

		int otp = random.nextInt(9000) + 100;
		ride.setOtp(otp);
		driverRepository.save(driver);

		rideRepository.save(ride);

	}

	@Override
	public void declineRide(Integer rideId, Integer driverId) throws RideException {

		Ride ride = findRideById(rideId);
		System.out.println(ride.getId());

		ride.getDeclinedDrivers().add(driverId);
		System.out.println(ride.getId() + "-" + ride.getDeclinedDrivers());

		List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(),
				ride.getPickupLongitude(), ride);

		Driver nearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(),
				ride.getPickupLongitude());

		ride.setDriver(nearestDriver);

		rideRepository.save(ride);

	}

	@Override
	public void startRide(Integer rideId, int otp) throws RideException {

		Ride ride = findRideById(rideId);

		if (otp != ride.getOtp()) {
			throw new RideException("Please provide a valid otp");

		}

		ride.setStatus(RideStatus.STARTED);
		ride.setStartTime(LocalDateTime.now());

		rideRepository.save(ride);

	}

	@Override
	public void completeRide(Integer rideId) throws RideException {

		Ride ride = findRideById(rideId);

		ride.setStatus(RideStatus.COMPLETED);
		ride.setEndTime(LocalDateTime.now());

		double distance = calculators.calculateDistance(ride.getDestinationLatitude(), ride.getDestinationLongitude(),
				ride.getPickupLatitude(), ride.getPickupLongitude());

		LocalDateTime start = ride.getStartTime();
		LocalDateTime end = ride.getEndTime();
		Duration duration = Duration.between(start, end);
		long milliSecond = duration.toMillis();

		System.out.println("duration -----------" + milliSecond);
		double fare = calculators.calculateFare(distance);

		ride.setDistance(Math.round(distance * 100.0) / 100.0);
		ride.setFare((int) Math.round(fare));
		ride.setDuration(milliSecond);
		ride.setEndTime(LocalDateTime.now());

		Driver driver = ride.getDriver();
		driver.getRides().add(ride);
		driver.setCurrentRide(null);

		Integer driversRevenue = (int) (driver.getTotalRevenue() + Math.round(fare * 0.8));
		driver.setTotalRevenue(driversRevenue);

		System.out.println(driver);

		rideRepository.save(ride);

	}

	@Override
	public void cancleRide(Integer rideId) throws RideException {

		Ride ride = findRideById(rideId);
		ride.setStatus(RideStatus.CANCELLED);
		rideRepository.save(ride);

	}

	@Override
	public Ride findRideById(Integer rideId) throws RideException {
		Optional<Ride> ride = rideRepository.findById(rideId);

		if (ride.isPresent()) {
			return ride.get();
		}
		throw new RideException("ride not exist id " + rideId);

	}

}
