package open.seats.tracker.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.request.TrackingRequest;
import open.seats.tracker.response.ApiResponse;
import open.seats.tracker.response.CatalogCourseResponse;
import open.seats.tracker.service.UserService;

@RestController
@RequestMapping("/track")
public class TrackingController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> track(@RequestBody TrackingRequest trackingRequest, @RequestHeader int userId)
			throws ValidationException {
		trackingRequest.setUserId(userId);
		userService.addTracking(trackingRequest);
		return ResponseEntity.ok(new ApiResponse("success"));
	}

	@GetMapping
	public ResponseEntity<?> getTracking(@RequestHeader int userId) {
		List<CatalogCourseResponse> courses = userService.getTracking(userId);
		return ResponseEntity.ok(new ApiResponse("success", courses));
	}

	@DeleteMapping
	public ResponseEntity<?> removeTracking(@RequestHeader int userId, @RequestParam String classNumber) {
		if (StringUtils.isBlank(classNumber)) {
			return new ResponseEntity(new ApiResponse("failed"), HttpStatus.BAD_REQUEST);
		}
		List<Integer> classNumbersList = Arrays.stream(classNumber.split(",")).map(Integer::parseInt)
				.collect(Collectors.toList());
		userService.removeTracking(userId, classNumbersList);
		return ResponseEntity.ok(new ApiResponse("success"));
	}

}
