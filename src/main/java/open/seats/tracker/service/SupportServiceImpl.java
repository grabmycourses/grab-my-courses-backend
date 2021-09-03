package open.seats.tracker.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import open.seats.tracker.model.Feedback;
import open.seats.tracker.repository.FaqsRepository;
import open.seats.tracker.repository.FeedbackRepository;
import open.seats.tracker.request.FeedbackRequest;
import open.seats.tracker.response.FaqResponse;

@Service("SupportService")
public class SupportServiceImpl implements SupportService {

	@Autowired
	private FaqsRepository faqsRepository;

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private Environment env;

	@Override
	public List<FaqResponse> getFaqs() {
		return faqsRepository.getFaqs();
	}

	@Override
	public void saveFeedback(FeedbackRequest request) {
		feedbackRepository.save(new Feedback(request.getType(), request.getEmail(), request.getComment(),
				Instant.now().toEpochMilli()));

		String body = "User feedback\n\nType: " + request.getType() + "\nUser email id: " + request.getEmail()
				+ "\nComment: " + request.getComment();

		notificationService.sendEmail("feedback", env.getProperty("feedback.to.address"),
				"User " + request.getEmail().substring(0, request.getEmail().indexOf("@")) + " feedback", body, false);
	}

}
