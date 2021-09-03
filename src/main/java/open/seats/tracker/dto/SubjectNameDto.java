package open.seats.tracker.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SubjectNameDto {
	@NonNull
	private String subjectCode;
	@NonNull
	private String subjectName;
}
