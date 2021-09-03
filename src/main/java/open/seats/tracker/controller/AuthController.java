package open.seats.tracker.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.request.ForgotPasswordRequest;
import open.seats.tracker.request.LoginRequest;
import open.seats.tracker.request.ResetPasswordRequest;
import open.seats.tracker.request.SignUpRequest;
import open.seats.tracker.response.ApiResponse;
import open.seats.tracker.security.JwtResponse;
import open.seats.tracker.security.JwtUtils;
import open.seats.tracker.security.UserDetailsImpl;
import open.seats.tracker.service.UserService;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	private static final String MESSAGE_SUCCESS = "success";

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) throws ValidationException {
		userService.signUp(signUpRequest);
		return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS));
	}

	@GetMapping("/register/confirm")
	public ResponseEntity<?> confirmAccountRegistration(@RequestParam("token") String token) {
		String response = userService.confirmAccountRegistration(token);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwtToken = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			if (BooleanUtils.isFalse(userDetails.isVerified())) {
				log.error("login failed; user {} email is not verified yet", loginRequest.getUsername());
				return new ResponseEntity(new ApiResponse(
						"Login failed. Your registered email is not verified yet. Please check your email inbox/spam folder for verification link."),
						HttpStatus.UNAUTHORIZED);
			}

			return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, new JwtResponse(jwtToken, userDetails.getUserId(),
					userDetails.getUsername(), userDetails.getFullname())));
		} catch (AuthenticationException e) {
			log.error("authentication failed for username {}; {}", loginRequest.getUsername(), e.getMessage());
			return new ResponseEntity(new ApiResponse("Login failed. Invalid email id or password."),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> requestResetPassword(@RequestBody @Valid ForgotPasswordRequest request)
			throws ValidationException {
		userService.requestResetPassword(request);
		return ResponseEntity.ok(new ApiResponse("Success"));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request)
			throws ValidationException {
		String response = userService.resetPassword(request);
		return ResponseEntity.ok(new ApiResponse(response));
	}

}
