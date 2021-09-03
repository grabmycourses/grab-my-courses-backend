package open.seats.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import open.seats.tracker.model.Tracking;
import open.seats.tracker.response.CatalogCourseResponse;

@Repository
public interface TrackingsRepository extends JpaRepository<Tracking, Integer> {

	@Query("SELECT new open.seats.tracker.response.CatalogCourseResponse(c.classNumber, CONCAT(s.subjectCode, ' ', c.courseNumber), "
			+ "c.classTitle, c.instructor, c.session) FROM Tracking t JOIN Course c "
			+ "ON t.classNumber=c.classNumber JOIN Subject s ON c.subjectId=s.subjectId "
			+ "WHERE t.userId=:userId and t.active=true AND c.active=true AND s.active=true ORDER BY c.courseNumber")
	public List<CatalogCourseResponse> fetchCoursesTrackedByUser(int userId);

	public long deleteByUserIdAndClassNumberAndActive(int userId, int classNumber, boolean active);
}
