package open.seats.tracker.response;

import java.util.Collections;

import lombok.Data;
import lombok.NonNull;

@Data
public class ApiResponse {
	@NonNull
	private String message;
	private Object responseData = Collections.emptyMap();

	public ApiResponse(String message, Object responseData) {
		this.message = message;
		this.responseData = responseData;
	}

	public ApiResponse(String message) {
		this.message = message;
	}
}
