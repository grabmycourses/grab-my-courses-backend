package open.seats.tracker.request;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import open.seats.tracker.util.SafeInput;

@Data
@NoArgsConstructor
public class ResetPasswordRequest {

	@SafeInput
	@NonNull
	@Size(min=4, message="Invalid request!")
	@Size(max=10, message="Invalid request!")
	private String otp;
	
	@SafeInput
	@NonNull
	@Size(min=4, message = "Password should be at least 4 characters long.")
	@Size(max=100, message = "Password can't be more than 100 characters long.")
	private String newPassword;
}
