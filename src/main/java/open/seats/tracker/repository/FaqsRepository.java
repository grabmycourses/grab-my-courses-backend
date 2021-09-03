package open.seats.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import open.seats.tracker.model.Faq;
import open.seats.tracker.response.FaqResponse;

@Repository
public interface FaqsRepository extends JpaRepository<Faq, Integer>{

	@Query("SELECT new open.seats.tracker.response.FaqResponse(question, answer) from Faq WHERE active=true ORDER BY displayOrder")
	List<FaqResponse> getFaqs();
}
