package open.seats.tracker.exception;

import lombok.Data;
import lombok.NonNull;

@Data
public class ValidationException extends Exception {
	private static final long serialVersionUID = -37800033991007L;
	
	@NonNull
	private final String message;
}
