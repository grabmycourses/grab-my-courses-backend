package open.seats.tracker.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CatalogCourseResponse {
	private int classNumber;
	private String course;
	private String classTitle;
	private String instructor;
	private String session;
}
