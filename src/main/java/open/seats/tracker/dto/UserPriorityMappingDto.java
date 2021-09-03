package open.seats.tracker.dto;

import lombok.Data;
import open.seats.tracker.model.User;

@Data
public class UserPriorityMappingDto {
	private int priority;
	private User user;
}