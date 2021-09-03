package open.seats.tracker.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SubjectLevelDto {
	@NonNull
	private String courseLevel;
	@NonNull
	private String subjectCode;
}
