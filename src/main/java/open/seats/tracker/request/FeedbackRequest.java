package open.seats.tracker.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NonNull;
import open.seats.tracker.util.SafeInput;

@Data
public class FeedbackRequest {
	
	@NonNull
	@NotEmpty(message = "Invalid feedback type!")
	@SafeInput
	private String type;
	
	@NonNull
	@NotEmpty(message = "Invalid feedback email!")
	@Email(message = "Invalid email")
	@Size(max = 100, message = "Only maximum of 100 characters are allowed in email")
	@SafeInput
	private String email;
	
	@NonNull
	@NotEmpty(message = "Invalid feedback content!")
	@Size(max = 5000, message = "Only maximum of 5000 characters are allowed in feedback")
	@SafeInput
	private String comment;
}
