package open.seats.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import open.seats.tracker.util.SafeInput;

@Data
public class ForgotPasswordRequest {

	@NotBlank(message = "Please provide a valid email.")
	@Email(message = "Please provide a valid email.")
	@SafeInput
	private String email;
}
