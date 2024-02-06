package com.cab.booking.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cab.booking.config.JwtUtil;
import com.cab.booking.exception.UserException;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.repo.UserRepository;
import com.cab.booking.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private JwtUtil jwtUtil;

//	@Override
//	public User createUser(User user) throws UserException {
//
//		return null;
//	}

	@Override
	public User getReqUserProfile(String token) throws UserException {

		String email = jwtUtil.getEmailFromJwt(token);
		User user = repository.findByEmail(email);

		if (user != null) {
			return user;
		}
		throw new UserException("Invalid token");
	}

	@Override
	public User findUserById(Integer userId) throws UserException {

		Optional<User> otp = repository.findById(userId);
		if (otp.isPresent()) {
			return otp.get();
		}
		throw new UserException("User not found with id " + userId);
	}

//	@Override
//	public User findUserByEmail(String email) throws UserException {
//
//		User otp = repository.findByEmail(email);
//		if (otp.isPresent()) {
//			return otp.get();
//		}
//		throw new UserException("User not found with id " + email);
//	}
//
//	@Override
//	public User findUserByToken(String token) throws UserException {
//
//
//		return null;
//	}

	@Override
	public List<Ride> completedRides(Integer userId) throws UserException {

		List<Ride> completedRides = repository.getCompletedRides(userId);

		return completedRides;
	}

}
