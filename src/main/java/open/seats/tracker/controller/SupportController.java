package open.seats.tracker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import open.seats.tracker.request.FeedbackRequest;
import open.seats.tracker.response.ApiResponse;
import open.seats.tracker.response.FaqResponse;
import open.seats.tracker.service.SupportService;

@RestController
@RequestMapping("/support")
public class SupportController {

	@Autowired
	private SupportService supportService;

	@GetMapping("/faqs")
	public ResponseEntity<?> getFaqs() {
		List<FaqResponse> response = supportService.getFaqs();
		return ResponseEntity.ok(new ApiResponse("successs", response));
	}

	@PostMapping("/feedback")
	public ResponseEntity<?> saveFeedback(@RequestBody @Valid FeedbackRequest request) {
		supportService.saveFeedback(request);
		return ResponseEntity.ok(new ApiResponse("successs"));
	}
}
