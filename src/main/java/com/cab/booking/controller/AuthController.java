package com.cab.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cab.booking.config.JwtUtil;
import com.cab.booking.domain.UserRole;
import com.cab.booking.exception.UserException;
import com.cab.booking.model.Driver;
import com.cab.booking.model.User;
import com.cab.booking.repo.DriverRepository;
import com.cab.booking.repo.UserRepository;
import com.cab.booking.request.DriversSignupRequest;
import com.cab.booking.request.LoginRequest;
import com.cab.booking.request.SignupRequest;
import com.cab.booking.response.JwtResponse;
import com.cab.booking.service.impl.CustomUserDetailsService;
import com.cab.booking.service.impl.DriverServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private UserRepository userRepository;
	private DriverRepository driverRepository;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;
	private CustomUserDetailsService customUserDetailsService;
	private DriverServiceImpl driverServiceImpl;

	public AuthController(UserRepository userRepository, DriverRepository driverRepository,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService,
			DriverServiceImpl driverServiceImpl) {
		super();
		this.userRepository = userRepository;
		this.driverRepository = driverRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
		this.driverServiceImpl = driverServiceImpl;
	}

	@PostMapping("/user/signup")
	public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest req) throws UserException {

		String email = req.getEmail();
		String fullName = req.getFullName();
		String mobile = req.getMobile();
		String password = req.getPassword();

		User user = userRepository.findByEmail(email);
		if (user != null) {

			throw new UserException("User Already Exist with email :: " + email);
		}
		String encodedPassword = passwordEncoder.encode(password);

		User createUser = new User();
		createUser.setEmail(email);
		createUser.setPassword(encodedPassword);
		createUser.setFullName(fullName);
		createUser.setMobile(mobile);
		createUser.setRole(UserRole.USER);

		User savedUser = userRepository.save(createUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtil.generateJwtToken(authentication);

		JwtResponse res = new JwtResponse();
		res.setJwt(jwt);
		res.setAuthenticated(true);
		res.setError(false);
		res.setErrorDetails(null);
		res.setType(UserRole.USER);
		res.setMessage("Account Created Successfully:" + savedUser.getFullName());

		return new ResponseEntity<JwtResponse>(res, HttpStatus.OK);

	}

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> signin(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Authentication authentication = authenticate(password, username);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtil.generateJwtToken(authentication);

		JwtResponse res = new JwtResponse();

		res.setJwt(jwt);
		res.setAuthenticated(true);
		res.setError(false);
		res.setErrorDetails(null);
		res.setType(UserRole.USER);
		res.setMessage("Account Login Successfully");
		return new ResponseEntity<JwtResponse>(res, HttpStatus.ACCEPTED);
	}

	@PostMapping("/driver/signup")
	public ResponseEntity<JwtResponse> driverSignup(@RequestBody DriversSignupRequest driverSignupRequest) {

		Driver driver = driverRepository.findByEmail(driverSignupRequest.getEmail());

//		if(driver!=null)

		JwtResponse jwtResponse = new JwtResponse();

		if (driver != null) {
			jwtResponse.setAuthenticated(false);
			jwtResponse.setErrorDetails("Email already used with another account");
			jwtResponse.setError(true);
			return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.BAD_GATEWAY);
		}

		Driver createDriver = driverServiceImpl.registerDriver(driverSignupRequest);

		Authentication authentication = new UsernamePasswordAuthenticationToken(createDriver.getEmail(),
				createDriver.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtil.generateJwtToken(authentication);

		jwtResponse.setJwt(jwt);
		jwtResponse.setAuthenticated(true);
		jwtResponse.setError(false);
		jwtResponse.setErrorDetails(null);
		jwtResponse.setType(UserRole.USER);
		jwtResponse.setMessage("Account Login Successfully");

		return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.ACCEPTED);
	}

	private Authentication authenticate(String password, String username) {

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username or password from authenticate method");
		}
		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Username or Password");

		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
