package open.seats.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import open.seats.tracker.model.Subject;

@Repository
public interface SubjectsRepository extends JpaRepository<Subject, Integer> {

}
