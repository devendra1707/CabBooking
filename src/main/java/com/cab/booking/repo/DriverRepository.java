package com.cab.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

	public Driver findByEmail(String email);

	@Query("SELECT R FROM Ride R WHERE R.status = REQUESTED AND R.driver.id=:driverId")
	public List<Ride> getAllocatedRides(Integer driverId);

	@Query("SELECT R FROM Ride R where R.status=COMPLETED AND R.driver.id=:driverId")
	public List<Ride> getCompletedRides(@Param("driverId") Integer driverId);
}
