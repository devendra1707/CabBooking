package com.cab.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cab.booking.response.MessageResponse;

@RestController
@RequestMapping
public class HomeController {
	@GetMapping(value = "/")
	public ResponseEntity<MessageResponse> homeController() {

		System.out.println("API Does ============================== Not get any response");

		MessageResponse messageResponse = new MessageResponse("Welcome Ola Backend System");

		return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
	}

	@GetMapping("/home")
	public String test() {
		return "Hello Programmer";
	}
}
