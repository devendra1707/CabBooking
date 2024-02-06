package com.cab.booking.model;

import java.util.List;

import com.cab.booking.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;
	private String email;
	private String mobile;
	private double rating;
	private double latitude;
	private double longitude;
	private String password;
	private String profilePicture;

	private UserRole role;

	@OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
	private License license;

	@JsonIgnore
	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ride> rides;

	@OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	private Vehicle vehicle;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private Ride currentRide;

	private Integer totalRevenue = 0;
}
