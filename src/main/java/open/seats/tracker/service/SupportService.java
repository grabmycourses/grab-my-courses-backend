package open.seats.tracker.service;

import java.util.List;

import open.seats.tracker.request.FeedbackRequest;
import open.seats.tracker.response.FaqResponse;

public interface SupportService {

	List<FaqResponse> getFaqs();

	void saveFeedback(FeedbackRequest request);

}
