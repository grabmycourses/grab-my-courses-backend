package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="feedback")
public class Feedback {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int feedbackId;
	private String type;
	private String senderEmail;
	private String comment;
	private long createdTimestamp;
	
	public Feedback(String type, String senderEmail, String comment, long time) {
		this.type = type;
		this.senderEmail=senderEmail;
		this.comment = comment;
		this.createdTimestamp = time;
	}
}