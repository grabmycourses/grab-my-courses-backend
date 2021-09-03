package open.seats.tracker.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ValidationResultDto {
	@NonNull
	private Boolean isValid;
	private String message;
	
	public ValidationResultDto(@NonNull Boolean valid) {
		this.isValid = valid;
	}
	
	public ValidationResultDto(@NonNull Boolean valid, String message) {
		this.isValid = valid;
		this.message = message;
	}
}
