package com.cab.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cab.booking.dtos.mapper.DtoMapper;
import com.cab.booking.exception.DriverException;
import com.cab.booking.exception.RideException;
import com.cab.booking.exception.UserException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.Ride;
import com.cab.booking.model.User;
import com.cab.booking.paylod.RideDTO;
import com.cab.booking.request.RideRequest;
import com.cab.booking.request.StartRideRequest;
import com.cab.booking.response.MessageResponse;
import com.cab.booking.service.DriverService;
import com.cab.booking.service.RideService;
import com.cab.booking.service.UserService;

@RestController
@RequestMapping("/api/rides")
public class RideController {

	@Autowired
	private RideService rideService;

	@Autowired
	private DriverService driverService;

	@Autowired
	private UserService userService;

	@PostMapping("/request")
	public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest,
			@RequestHeader("Authorization") String jwt) throws UserException, DriverException {
		User user = userService.getReqUserProfile(jwt);

		Ride ride = rideService.requestRide(rideRequest, user);
		RideDTO rideDto = DtoMapper.toRideDto(ride);
		return new ResponseEntity<>(rideDto, HttpStatus.ACCEPTED);

	}

	@PutMapping("/{rideId}/accept")
	public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId)
			throws UserException, RideException {
		rideService.acceptRide(rideId);
		MessageResponse res = new MessageResponse("Ride Accepted By Driver");
		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{rideId}/decline")
	public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Integer rideId) throws UserException, DriverException, RideException {
		Driver driver = driverService.getReqDriverProfile(jwt);
		rideService.declineRide(rideId, driver.getId());
		MessageResponse res = new MessageResponse("Ride Decline By Driver");

		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

	}

	@PutMapping("/{rideId}/start")
	public ResponseEntity<MessageResponse> rideStartHandler(@PathVariable Integer rideId,
			@RequestBody StartRideRequest req) throws RideException {
		rideService.startRide(rideId, req.getOtp());
		MessageResponse res = new MessageResponse("Ride is started");
		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{rideId}/complete")
	public ResponseEntity<MessageResponse> rideCompleteHandler(@PathVariable Integer rideId) throws RideException {
		rideService.completeRide(rideId);
		MessageResponse res = new MessageResponse("Ride is Completed THank you For Booking Cab");
		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{rideId}")
	public ResponseEntity<RideDTO> findRideByIdHandler(@PathVariable Integer rideId,
			@RequestHeader("Authorization") String jwt) throws UserException, RideException {
//		User user = userService.getReqUserProfile(jwt);
		Ride ride = rideService.findRideById(rideId);
		RideDTO rideDTO = DtoMapper.toRideDto(ride);
		return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
	}

}
