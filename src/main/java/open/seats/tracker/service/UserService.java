package open.seats.tracker.service;

import java.util.List;

import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.request.ForgotPasswordRequest;
import open.seats.tracker.request.ResetPasswordRequest;
import open.seats.tracker.request.SignUpRequest;
import open.seats.tracker.request.TrackingRequest;
import open.seats.tracker.response.CatalogCourseResponse;

public interface UserService {
	
	void signUp(SignUpRequest signUpRequest) throws ValidationException;

	void addTracking(TrackingRequest trackingRequest) throws ValidationException;

	List<CatalogCourseResponse> getTracking(int userId);

	void removeTracking(int userId, List<Integer> classNumbersList);

	String confirmAccountRegistration(String token);

	void requestResetPassword(ForgotPasswordRequest request) throws ValidationException;

	String resetPassword(ResetPasswordRequest request) throws ValidationException;
}
