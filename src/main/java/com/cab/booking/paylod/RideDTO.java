package com.cab.booking.paylod;

import java.time.LocalDateTime;

import com.cab.booking.domain.RideStatus;
import com.cab.booking.model.PaymentDetails;

import lombok.Data;

@Data
public class RideDTO {

	private Integer id;
	private UserDTO user;
	private DriverDto driverDto;
	private double pickupLatitude;
	private double pickupLongitude;
	private double destinationLatitude;
	private double destinationLongitude;
	private String pickupArea;
	private String destinationArea;
	private double distance;
	private RideStatus status;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private long duration;
	private double fare;
	private PaymentDetails details;
	private int otp;
//	private PaymentDetails paymentDetails = new PaymentDetails();

}
