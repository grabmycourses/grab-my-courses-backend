package open.seats.tracker.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropOptionsResponse {
	@NonNull
	private String dropperEmail;
	@NonNull
	private String dropperDropOptions;
}
