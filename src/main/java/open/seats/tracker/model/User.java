package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name="users")
@Audited
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userId;
	
	@NonNull
	private String fullName;
	
	@NonNull
	@Email
	private String email;
	
	@NonNull
	private String password;
	
	private boolean active = true;
	private boolean dropSharing = true;
	private boolean verified = false;
	private long createdTimestamp;
	private long lastModifiedTimestamp;
}
