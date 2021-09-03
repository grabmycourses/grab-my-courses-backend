package open.seats.tracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.request.AddDropOptionRequest;
import open.seats.tracker.request.RemoveDropOptionsRequest;
import open.seats.tracker.request.UpdatePrivacyRequest;
import open.seats.tracker.response.ApiResponse;
import open.seats.tracker.response.SwapPreferencesResponse;
import open.seats.tracker.service.SwappingService;
import open.seats.tracker.util.ValidationUtils;

@RestController
@RequestMapping("/swapping/options")
public class SwapPrefController {

	@Autowired
	private ValidationUtils validationUtils;

	@Autowired
	private SwappingService swappingService;

	@GetMapping
	public ResponseEntity<?> fetchSwapOptions(@RequestHeader int userId) throws ValidationException {
		SwapPreferencesResponse response = swappingService.fetchUserSwapOptions(userId, null);
		return ResponseEntity.ok(new ApiResponse("success", response));
	}

	@PostMapping
	public ResponseEntity<?> addDropOptions(@RequestBody @Valid AddDropOptionRequest request, @RequestHeader int userId)
			throws ValidationException {
		request.setUserId(userId);
		validationUtils.validateAddDropOptionRequest(request);
		SwapPreferencesResponse response = swappingService.addUserDropOption(request);
		return ResponseEntity.ok(new ApiResponse("success", response));
	}

	@DeleteMapping
	public ResponseEntity<?> removeDropOptions(@RequestBody @Valid RemoveDropOptionsRequest request,
			@RequestHeader int userId) throws ValidationException {
		request.setUserId(userId);
		validationUtils.validateRemoveDropOptionsRequest(request);
		SwapPreferencesResponse response = swappingService.removeUserDropOptions(request);
		return ResponseEntity.ok(new ApiResponse("success", response));
	}

	@PostMapping("/sharing")
	public ResponseEntity<?> updatePrivacySetting(@RequestBody UpdatePrivacyRequest request, @RequestHeader int userId)
			throws ValidationException {
		request.setUserId(userId);
		SwapPreferencesResponse response = swappingService.updatePrivacySetting(request);
		if (response == null) {
			return ResponseEntity.ok(new ApiResponse("success"));
		} else {
			return ResponseEntity.ok(new ApiResponse("success", response));
		}
	}
}
