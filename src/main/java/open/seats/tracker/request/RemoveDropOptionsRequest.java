package open.seats.tracker.request;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class RemoveDropOptionsRequest {
	
	private int userId;
	
	@NonNull
	private List<String> dropOptions;
}
