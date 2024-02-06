package com.cab.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cab.booking.model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {

}
