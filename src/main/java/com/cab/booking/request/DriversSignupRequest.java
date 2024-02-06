package com.cab.booking.request;

import com.cab.booking.model.License;
import com.cab.booking.model.Vehicle;

import lombok.Data;

@Data
public class DriversSignupRequest {

	private String name;
	private String email;
	private String mobile;
	private String pasword;
	private double latitude;
	private double longitude;
	
	private License license;
	private Vehicle vehicle;

}
