package open.seats.tracker.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import open.seats.tracker.dto.ValidationResultDto;
import open.seats.tracker.dto.ValueLabelDto;
import open.seats.tracker.request.SearchCatalogRequest;
import open.seats.tracker.response.ApiResponse;
import open.seats.tracker.response.CatalogCourseResponse;
import open.seats.tracker.service.TrackingService;
import open.seats.tracker.util.ValidationUtils;

@RestController
public class SearchController {

	@Autowired
	private ValidationUtils validationUtils;

	@Autowired
	private TrackingService trackingService;

	@PostMapping("/search")
	public ResponseEntity<?> searchCatalog(@RequestBody SearchCatalogRequest request) {

		ValidationResultDto validationResult = validationUtils.validateSearchCatalogRequest(request);
		if (BooleanUtils.isFalse(validationResult.getIsValid())) {
			return new ResponseEntity(new ApiResponse(validationResult.getMessage()), HttpStatus.BAD_REQUEST);
		} else {
			List<CatalogCourseResponse> searchResult = trackingService.searchCatalog(request);
			if (CollectionUtils.isEmpty(searchResult)) {
				return ResponseEntity.ok(new ApiResponse(
						"Failed to fetch any matching results. Please modify your search criteria and try again.",
						searchResult));
			} else {
				return ResponseEntity.ok(new ApiResponse("success", searchResult));
			}
		}
	}

	@GetMapping("/subjects")
	public ResponseEntity<?> getSubjectsData() {
		List<ValueLabelDto> data = trackingService.getSubjectsData();
		return ResponseEntity.ok(new ApiResponse("success", data));
	}
}
