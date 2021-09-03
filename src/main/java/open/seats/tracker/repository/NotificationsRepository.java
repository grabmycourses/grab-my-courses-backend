package open.seats.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import open.seats.tracker.model.Notification;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Integer> {

	public Notification findFirstByUserIdAndClassNumberAndModeAndTypeOrderByTimestampDesc(int userId, int classNumber, int mode, int type);
}