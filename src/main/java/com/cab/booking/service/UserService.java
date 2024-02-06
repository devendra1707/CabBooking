package com.cab.booking.service;

import java.util.List;

import com.cab.booking.exception.UserException;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;

public interface UserService {

//	public User createUser(User user) throws UserException;

	public User getReqUserProfile(String token) throws UserException;

	public User findUserById(Integer userId) throws UserException;

//	public User findUserByEmail(String email) throws UserException;
//
//	public User findUserByToken(String token) throws UserException;

	public List<Ride> completedRides(Integer userId) throws UserException;

}
