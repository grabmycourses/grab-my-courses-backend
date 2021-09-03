package open.seats.tracker.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import open.seats.tracker.dto.DropOptionsDto;
import open.seats.tracker.dto.ValueLabelDto;

@Data
@NoArgsConstructor
public class SwapPreferencesResponse {
	private boolean sharingEnabled;
	private List<DropOptionsDto> swapOptions;
	private List<ValueLabelDto> dropOptions;

	public SwapPreferencesResponse(boolean sharingEnabled) {
		this.sharingEnabled = sharingEnabled;
		this.swapOptions = new ArrayList<>();
		this.dropOptions = new ArrayList<>();
	}

	public SwapPreferencesResponse(boolean sharingEnabled, List<DropOptionsDto> swapOptions, List<ValueLabelDto> dropOptions) {
		this.sharingEnabled = sharingEnabled;
		this.swapOptions = swapOptions;
		this.dropOptions = dropOptions;
	}
	
}
