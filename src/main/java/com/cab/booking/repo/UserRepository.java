package com.cab.booking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cab.booking.model.Ride;
import com.cab.booking.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);

	
	@Query("SELECT R FROM Ride R where R.status=COMPLETED AND R.user.id=:userId")
	public List<Ride> getCompletedRides(@Param("userId") Integer userId);
	
	
}
