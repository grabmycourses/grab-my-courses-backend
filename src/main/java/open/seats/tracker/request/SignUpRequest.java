package open.seats.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import open.seats.tracker.util.SafeInput;

@Data
public class SignUpRequest {
	
	@NotBlank(message = "Please provide a valid full name.")
	@Size(min=2, message = "Name should be at least 2 characters.")
	@Size(max=100, message = "Name can't exceed 100 characters.")
	@SafeInput
	private String fullName;
	
	@NotBlank(message = "Please provide a valid email.")
	@Email(message = "Please provide a valid email.")
	@SafeInput
	private String email;
	
	@NotBlank(message="Please provide some password.")
	@Size(min=4, message = "Password should be at least 4 characters long.")
	@Size(max=100, message = "Password can't be more than 100 characters long.")
	@SafeInput
	private String password;
}
