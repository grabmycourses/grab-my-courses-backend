package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="courses")
public class Course {
	@Id
	private int classNumber;
	private int subjectId;
	private String courseNumber;
	private String classTitle;
	private String instructor;
	private int totalSeats;
	private int openSeats;
	private boolean onlineCourse;
	private String session;
	private String courseLevel;
	private boolean active;
	private long createdTimestamp;
	private long lastModifiedTimestamp;
}
