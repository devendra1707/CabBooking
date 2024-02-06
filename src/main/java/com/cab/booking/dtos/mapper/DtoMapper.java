package com.cab.booking.dtos.mapper;

import org.springframework.context.annotation.Configuration;

import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.paylod.DriverDto;
import com.cab.booking.paylod.RideDTO;
import com.cab.booking.paylod.UserDTO;


@Configuration
public class DtoMapper {

	public static DriverDto toDriverDTO(Driver driver) {
		DriverDto driverDto = new DriverDto();

		driverDto.setEmail(driver.getEmail());
		driverDto.setId(driver.getId());
		driverDto.setLatitude(driver.getLatitude());
		driverDto.setLongitude(driver.getLongitude());
		driverDto.setMobile(driver.getMobile());
		driverDto.setName(driver.getName());
		driverDto.setRating(driver.getRating());
		driverDto.setRole(driver.getRole());
		driverDto.setVehicle(driver.getVehicle());
		return driverDto;
	}

	public static UserDTO toUser(User user) {
		UserDTO userDto = new UserDTO();
		userDto.setEmail(user.getEmail());
		userDto.setMobile(user.getMobile());
		userDto.setName(user.getFullName());
		return userDto;
	}

	public static RideDTO toRideDto(Ride ride) {
		DriverDto driverDto = new DriverDto();
		UserDTO userDto = toUser(ride.getUser());

		RideDTO rideDto = new RideDTO();

		rideDto.setDestinationLatitude(ride.getDestinationLatitude());
		rideDto.setDestinationLongitude(ride.getDestinationLongitude());
		ride.setDistance(ride.getDistance());
		rideDto.setDriverDto(driverDto);
		rideDto.setDuration(ride.getDuration());
		rideDto.setEndTime(ride.getEndTime());
		rideDto.setId(ride.getId());
		rideDto.setStartTime(ride.getStartTime());
		rideDto.setFare(ride.getFare());
		rideDto.setPickupLatitude(ride.getPickupLatitude());
		rideDto.setPickupLongitude(ride.getPickupLongitude());
		rideDto.setStartTime(ride.getStartTime());
		rideDto.setUser(userDto);
		rideDto.setPickupArea(ride.getPickupArea());
		rideDto.setDestinationArea(ride.getDestinationArea());
//		rideDto.setPaymentDetails(ride.getPaymentDetails());
		ride.setOtp(ride.getOtp());
		return rideDto;

	}

}
