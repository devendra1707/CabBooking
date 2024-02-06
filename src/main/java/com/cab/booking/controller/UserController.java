package com.cab.booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cab.booking.exception.UserException;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer userId) throws UserException {
		System.out.println("FInd by user Id");
		User createdUser = userService.findUserById(userId);

		return new ResponseEntity<User>(createdUser, HttpStatus.ACCEPTED);

	}

	@GetMapping("/profile")
	public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization") String jwt)
			throws UserException {
		User user = userService.getReqUserProfile(jwt);
		return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getcompletedRidesHandler(@RequestHeader("Authorization") String jwt)
			throws UserException {
		User user = userService.getReqUserProfile(jwt);
		List<Ride> rides = userService.completedRides(user.getId());
		return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
	}

}
