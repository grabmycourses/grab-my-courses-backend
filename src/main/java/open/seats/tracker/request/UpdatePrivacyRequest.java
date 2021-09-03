package open.seats.tracker.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePrivacyRequest {
	private int userId;
	private boolean sharingEnabled=true;
}
