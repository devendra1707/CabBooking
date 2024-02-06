package com.cab.booking.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RideRequest {

	private double pickupLatitude;
	private double pickupLongitude;
	private double distinationLatitude;
	private double distinationLongitude;
	private String pickupArea;
	private String destinationArea;

}
