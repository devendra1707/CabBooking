package com.cab.booking.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {

	@NotBlank(message = "Email is Required")
	@Email(message = "Email should be valid")
	private String email;

	private String fullName;

	private String password;

	private String mobile;

}
