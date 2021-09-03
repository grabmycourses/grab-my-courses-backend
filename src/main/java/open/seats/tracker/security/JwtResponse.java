package open.seats.tracker.security;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtResponse {
	@NonNull
	private String token;

	private String type = "Bearer";
	
	@NonNull
	private int userId;
	
	@NonNull
	private String username;
	
	@NonNull
	private String fullname;
}
