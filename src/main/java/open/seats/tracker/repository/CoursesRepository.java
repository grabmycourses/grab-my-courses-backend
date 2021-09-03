package open.seats.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import open.seats.tracker.dto.SubjectLevelDto;
import open.seats.tracker.dto.UserTrackedClassesDto;
import open.seats.tracker.model.Course;
import open.seats.tracker.response.CatalogCourseResponse;

@Repository
public interface CoursesRepository extends JpaRepository<Course, Integer>{

	@Query("SELECT new open.seats.tracker.response.CatalogCourseResponse(c.classNumber, CONCAT(s.subjectCode, ' ', c.courseNumber), "
			+ "c.classTitle, c.instructor, c.session) FROM Course c JOIN Subject s "
			+ "ON c.subjectId=s.subjectId WHERE c.subjectId=:subjectId AND c.courseNumber LIKE :courseNumPattern "
			+ "AND c.onlineCourse=:online AND c.session=:session AND c.active=true ORDER BY c.courseNumber")
	public List<CatalogCourseResponse> fetchCoursesWithSession(int subjectId, String courseNumPattern, String session, boolean online);
	
	@Query("SELECT new open.seats.tracker.response.CatalogCourseResponse(c.classNumber, CONCAT(s.subjectCode, ' ', c.courseNumber), "
			+ "c.classTitle, c.instructor, c.session) FROM Course c JOIN Subject s "
			+ "ON c.subjectId=s.subjectId WHERE c.subjectId=:subjectId AND c.courseNumber LIKE :courseNumPattern "
			+ "AND c.onlineCourse=:online AND c.active=true ORDER BY c.courseNumber")
	public List<CatalogCourseResponse> fetchCoursesWithoutSession(int subjectId, String courseNumPattern, boolean online);
	
	@Query("SELECT Count(c) FROM Course c WHERE c.subjectId=:subjectId AND c.courseLevel=:level AND c.onlineCourse=:online AND c.active=true")
	public int getCountInCourseLevel(int subjectId, String level, boolean online);
	
	@Query("SELECT DISTINCT new open.seats.tracker.dto.SubjectLevelDto(c.courseLevel, s.subjectCode) FROM Course c JOIN Subject s "
			+ "ON c.subjectId=s.subjectId JOIN Tracking t ON c.classNumber=t.classNumber WHERE c.onlineCourse=false "
			+ "GROUP BY c.courseLevel, c.subjectId ORDER BY c.courseLevel")
	public List<SubjectLevelDto> getLevelwiseTrackedInPersonSubjects();
	
	@Query("SELECT new open.seats.tracker.dto.UserTrackedClassesDto(t.classNumber, t.priority, u) FROM Tracking t JOIN User u ON t.userId=u.userId "
			+ "WHERE t.active=true and u.active=true ORDER BY t.priority DESC")
	public List<UserTrackedClassesDto> getTrackedClassesForUsers();
}
