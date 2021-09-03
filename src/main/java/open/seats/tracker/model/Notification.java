package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int notificationId;
	
	private int mode;
	private int userId;
	private int type;
	private int classNumber;
	private long timestamp;
	
	public Notification(int mode, int userId, int type, int classNumber, long timestamp) {
		this.mode = mode;
		this.userId = userId;
		this.type = type;
		this.classNumber = classNumber;
		this.timestamp = timestamp;
	}
}
