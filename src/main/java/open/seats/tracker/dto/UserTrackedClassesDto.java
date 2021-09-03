package open.seats.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import open.seats.tracker.model.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserTrackedClassesDto {
	private int classNumber;
	private int priority;
	private User user;
}
