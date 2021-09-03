package open.seats.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import open.seats.tracker.util.SafeInput;

@Data
@AllArgsConstructor
public class LoginRequest {
	
	@NotBlank(message = "Please provide a valid email.")
	@Email(message = "Please provide a valid email.")
	@SafeInput
	private String username;
	
	@NotBlank(message="Please provide a valid password.")
	@SafeInput
 	private String password;
}
