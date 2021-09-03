package open.seats.tracker.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ValueLabelDto {
	@NonNull
	private String value;
	@NonNull
	private String label;
}
