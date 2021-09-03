package open.seats.tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import open.seats.tracker.dto.SwapPreferencesDto;
import open.seats.tracker.model.DropOption;

@Repository
public interface DropOptionsRepository extends JpaRepository<DropOption, Integer> {

	List<DropOption> getByUserId(int userId);

	public long deleteByUserIdAndClassNumberAndActive(int userId, int classNumber, boolean active);

	@Query(value = "SELECT dropData.trackingClassNumber, u.email as swappingUserEmail, dropData.swappingClassNumber, c.subjectId as swappingSubjectId, "
			+ "c.courseNumber as swappingCourseNumber FROM (SELECT t1.classNumber as trackingClassNumber, d1.userId as swappingUserId, t2.classNumber "
			+ "AS swappingClassNumber FROM trackings t1 JOIN drop_options d1 ON t1.classNumber=d1.classNumber LEFT OUTER JOIN trackings t2 ON "
			+ "d1.userId=t2.userId WHERE t1.userId=:userId AND (t2.classNumber IS NULL OR t2.classNumber IN (select classNumber from drop_options "
			+ "where userId=:userId and active=true)) and t1.active=true and (t2.active=true or t2.active is null) and d1.active=true) AS dropData "
			+ "JOIN users u ON dropData.swappingUserId=u.userId LEFT OUTER JOIN courses c ON dropData.swappingClassNumber=c.classNumber "
			+ "WHERE u.active=true AND u.dropSharing=true AND (c.active=true or c.active is null) ORDER By dropData.trackingClassNumber", nativeQuery=true)
	List<SwapPreferencesDto> getUserSwapOptions(int userId);
	
}