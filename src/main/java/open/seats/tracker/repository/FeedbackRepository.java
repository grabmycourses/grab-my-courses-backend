package open.seats.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import open.seats.tracker.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
	
}
