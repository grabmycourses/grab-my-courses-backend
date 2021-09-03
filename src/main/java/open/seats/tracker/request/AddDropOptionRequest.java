package open.seats.tracker.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import open.seats.tracker.util.SafeInput;

@Data
@NoArgsConstructor
public class AddDropOptionRequest {
	
	private int userId;
	
	@NonNull
	@SafeInput
	private String dropOption;
}
