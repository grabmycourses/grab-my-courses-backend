package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="subjects")
public class Subject {
	
	@Id
	private int subjectId;
	
	private String subjectCode;
	private String subjectName;
	private boolean active;
	private long createdTimestamp;
	private long lastModifiedTimestamp;
}
