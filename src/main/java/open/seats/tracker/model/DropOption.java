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
@Table(name="drop_options")
public class DropOption {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int dropOptionId;
	private int classNumber;
	private int userId;
	private boolean active = true;
	private long createdTimestamp;
	private long lastModifiedTimestamp;
	
	public DropOption(int userId, int classNumber) {
		this.classNumber = classNumber;
		this.userId = userId;
		this.createdTimestamp = Instant.now().toEpochMilli();
		this.lastModifiedTimestamp = Instant.now().toEpochMilli();
	}
}
