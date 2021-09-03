package open.seats.tracker.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import open.seats.tracker.response.DropOptionsResponse;

@Data
@NoArgsConstructor
public class DropOptionsDto {
	private int trackedClassNumber;
	private List<DropOptionsResponse> dropDetails;
	
	public DropOptionsDto(int trackedClassNumber, List<DropOptionsResponse> dropDetails) {
		this.trackedClassNumber = trackedClassNumber;
		this.dropDetails = dropDetails;
	}
	
}
