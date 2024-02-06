package com.cab.booking.paylod;

import com.cab.booking.domain.UserRole;
import com.cab.booking.model.Vehicle;

import lombok.Data;

@Data
public class DriverDto {

	private Integer id;
	private String name;
	private String email;
	private String mobile;
	private double rating;
	private double latitude;
	private double longitude;
	private UserRole role;
	private Vehicle vehicle;
}
