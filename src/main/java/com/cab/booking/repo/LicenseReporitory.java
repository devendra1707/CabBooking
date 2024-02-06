package com.cab.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cab.booking.model.License;

@Repository
public interface LicenseReporitory extends JpaRepository<License, Integer> {

}
