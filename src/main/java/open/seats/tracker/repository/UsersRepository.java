package open.seats.tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import open.seats.tracker.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

	List<User> findByEmailAndActiveOrderByCreatedTimestampDesc(String email, boolean activeStatus);
	
	Optional<User> findByEmailAndActiveAndVerified(String email, boolean active, boolean verified);
	
	boolean existsByEmailAndVerifiedAndActive(String email, boolean verifiedStatus, boolean activeStatus);
}
