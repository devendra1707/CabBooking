package com.cab.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class License {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String licenseNumber;
	private String licenseState;
	private String licenseExpirationDate;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private Driver driver;
}
