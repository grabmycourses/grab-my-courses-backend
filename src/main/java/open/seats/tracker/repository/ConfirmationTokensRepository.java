package open.seats.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import open.seats.tracker.model.ConfirmationToken;

public interface ConfirmationTokensRepository extends JpaRepository<ConfirmationToken, Integer> {
	
	ConfirmationToken findByTokenAndTypeAndActive(String token, int type, boolean active);
	
	ConfirmationToken findTopByUserIdAndTypeOrderByCreatedAtDesc(int userId, int type);

	boolean existsConfirmationTokenByTokenAndTypeAndActive(String token, int type, boolean active);

}
