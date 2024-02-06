package com.cab.booking.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cab.booking.config.JwtUtil;
import com.cab.booking.domain.RideStatus;
import com.cab.booking.domain.UserRole;
import com.cab.booking.exception.DriverException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.License;
import com.cab.booking.model.Ride;
import com.cab.booking.model.Vehicle;
import com.cab.booking.repo.DriverRepository;
import com.cab.booking.repo.LicenseReporitory;
import com.cab.booking.repo.RideRepository;
import com.cab.booking.repo.VehicleRepository;
import com.cab.booking.request.DriversSignupRequest;
import com.cab.booking.service.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	private DriverRepository driverRepository;
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
	@Autowired
	private RideRepository rideRepository;
	@Autowired
	private Calculators calculators;

	@Override
	public Driver registerDriver(DriversSignupRequest driversSignupRequest) {

		License license = driversSignupRequest.getLicense();
		Vehicle vehicle = driversSignupRequest.getVehicle();

		License createdLicense = new License();

		createdLicense.setLicenseState(license.getLicenseState());
		createdLicense.setLicenseNumber(license.getLicenseNumber());
		createdLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
		createdLicense.setId(license.getId());

		License savedLicense = licenseReporitory.save(createdLicense);

		Vehicle createdVehicle = new Vehicle();

		createdVehicle.setCapacity(vehicle.getCapacity());
		createdVehicle.setColor(vehicle.getColor());
		createdVehicle.setId(vehicle.getId());
		createdVehicle.setMake(vehicle.getMake());
		createdVehicle.setLicensePlate(vehicle.getLicensePlate());
		createdVehicle.setYear(vehicle.getYear());
		createdVehicle.setModel(vehicle.getModel());

		Vehicle savedVehicle = vehicleRepository.save(createdVehicle);

		Driver driver = new Driver();

		String encodedPassword = passwordEncoder.encode(driversSignupRequest.getPasword());

		driver.setEmail(driversSignupRequest.getEmail());
		driver.setName(driversSignupRequest.getName());
		driver.setMobile(driversSignupRequest.getMobile());
		driver.setPassword(encodedPassword);
		driver.setLicense(savedLicense);
		driver.setVehicle(savedVehicle);
		driver.setRole(UserRole.DRIVER);

		driver.setLatitude(driversSignupRequest.getLatitude());
		driver.setLongitude(driversSignupRequest.getLongitude());

		Driver createdDriver = driverRepository.save(driver);

		savedLicense.setDriver(createdDriver);
		savedVehicle.setDriver(createdDriver);

		licenseReporitory.save(savedLicense);
		vehicleRepository.save(createdVehicle);

		return createdDriver;
	}

	@Override
	public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride) {

		List<Driver> allDrivers = driverRepository.findAll();

		List<Driver> availableDriver = new ArrayList<>();

		for (Driver driver : allDrivers) {
			if (driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED) {
				continue;
			}
			if (ride.getDeclinedDrivers().contains(driver.getRides())) {
				System.out.println("its continue");
				continue;
			}
			double driverLatitude = driver.getLatitude();
			double driverLongitude = driver.getLongitude();

			double distance = calculators.calculateDistance(driverLatitude, driverLongitude, pickupLatitude,
					pickupLongitude);

			availableDriver.add(driver);
		}

		return availableDriver;
	}

	@Override
	public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {

		double min = Double.MAX_VALUE;
		Driver nearestDriver = null;

		for (Driver driver : availableDrivers) {
			double driverLatitude = driver.getLatitude();
			double driverLongitude = driver.getLongitude();

			double distance = dictanceCalculator.calculateDistance(pickupLatitude, driverLongitude, driverLatitude,
					driverLongitude);

			if (min > distance) {
				min = distance;
				nearestDriver = driver;
			}
		}

		return nearestDriver;
	}

	@Override
	public Driver getReqDriverProfile(String jwt) throws DriverException {

		String email = jwtUtil.getEmailFromJwt(jwt);
		Driver driver = driverRepository.findByEmail(email);
		if (driver == null) {
			throw new DriverException("Driver not exist with email :: " + email);
		}

		return driver;
	}

	@Override
	public Ride getDriversCurrentRide(Integer driverId) throws DriverException {

		Driver driver = findDriverById(driverId);

		return driver.getCurrentRide();
	}

	@Override
	public List<Ride> getAllocatedRides(Integer driverId) throws DriverException {

		List<Ride> allocatedRides = driverRepository.getAllocatedRides(driverId);
		return allocatedRides;
	}

	@Override
	public Driver findDriverById(Integer driverId) throws DriverException {

		Optional<Driver> optional = driverRepository.findById(driverId);
		if (optional.isPresent()) {
			return optional.get();
		}

		throw new DriverException("Driver not exist with id :: " + driverId);
	}

	@Override
	public List<Ride> completedRides(Integer driverId) throws DriverException {

		List<Ride> completedRides = driverRepository.getCompletedRides(driverId);
		return completedRides;
	}

}
