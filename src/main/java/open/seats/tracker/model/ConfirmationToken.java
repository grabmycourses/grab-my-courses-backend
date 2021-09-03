package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="confirmation_tokens")
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private int type;
    private int userId;
    private long createdAt;
    private Long confirmedAt;
    private boolean active=true;
    
    public ConfirmationToken(String token, int type, int userId, long createdAt) {
    	this.token = token;
    	this.type = type;
    	this.userId = userId;
    	this.createdAt = createdAt;
    }
}
