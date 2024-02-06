package com.cab.booking.paylod;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {

	private Integer id;
	
	private String name;

	private String email;

	private String mobile;


}
