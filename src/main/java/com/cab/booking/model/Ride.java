package com.cab.booking.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import com.cab.booking.domain.RideStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	private Driver driver;

	@JsonIgnore
	private List<Integer> declinedDrivers = new ArrayList<>();

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

	private int otp;

	@Embedded
	private PaymentDetails paymentDetails = new PaymentDetails();

}
