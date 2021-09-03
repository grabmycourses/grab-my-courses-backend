package open.seats.tracker.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Audited
@Table(name="trackings")
public class Tracking {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int trackingId;
	private int classNumber;
	private int userId;
	private int priority=1;
	private boolean active = true;
	private long createdTimestamp;
	private long lastModifiedTimestamp;
	
	public Tracking(int userId, int classNumber) {
		this.classNumber = classNumber;
		this.userId = userId;
		this.createdTimestamp = Instant.now().toEpochMilli();
		this.lastModifiedTimestamp = Instant.now().toEpochMilli();
	}
}
