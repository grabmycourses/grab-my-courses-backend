package open.seats.tracker.request;

import lombok.Data;

@Data
public class SearchCatalogRequest {
	private int subjectId; // eg: 19 for CSE - mandatory
	
	private String courseNumber; // 3 digit eg: 546 - mandatory 
	
	private String session; // eg: "C" for session C - not mandatory
	
	private boolean onlineCourse; // default is offline
}
