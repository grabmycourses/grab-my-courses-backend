package open.seats.tracker.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.dto.ValidationResultDto;
import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.request.AddDropOptionRequest;
import open.seats.tracker.request.RemoveDropOptionsRequest;
import open.seats.tracker.request.SearchCatalogRequest;

@Component
@Log4j2
public class ValidationUtils {
	
	@Autowired
	private DataUtils dataUtils;

	public ValidationResultDto validateSearchCatalogRequest(SearchCatalogRequest request) {
		if(dataUtils.getSubjectsData().containsKey(request.getSubjectId())) {
			if(StringUtils.isNotBlank(request.getCourseNumber()) && isValidSubjectNumber(request.getCourseNumber())) {
				if(StringUtils.isNotBlank(request.getSession()) && !dataUtils.sessionsList.contains(request.getSession())) {
					log.error("invalid session code");
					return new ValidationResultDto(false, "Invalid request!");
				}
				log.info("validation successful");
				return new ValidationResultDto(true);
			} else {
				return new ValidationResultDto(false, "Please enter a valid course number/pattern.");
			}
		} else {
			log.error("invalid subject id");
			return new ValidationResultDto(false, "Invalid request!");
		}
	}
	
	public boolean isValidSubjectNumber(String subjectNum) {
		return subjectNum.matches("^[1-9][\\d\\*][\\d\\*]$");
		// Can test for {"1**", "234", "5436", "*45", "**4", "***3", "****", "045", "100", "34*"} : some are valid, others are invalid.
	}
	
	public void validateAddDropOptionRequest(AddDropOptionRequest request) throws ValidationException {
		String classNumberRegex = "^[0-9]{5}$";
		if (request.getUserId() <= 0) {
			log.error("invalid userId");
			throw new ValidationException("Invalid input");
		}
		if (!request.getDropOption().matches(classNumberRegex)) {
			log.error("invalid classNumber");
			throw new ValidationException("Invalid input");
		}
	}

	public void validateRemoveDropOptionsRequest(RemoveDropOptionsRequest request) throws ValidationException {
		String classNumberRegex = "^[0-9]{5}$";
		if(request.getUserId() <= 0) {
			log.error("invalid userId");
			throw new ValidationException("Invalid input");
		}
		for(String classNumberStr : request.getDropOptions()) {
			if(!classNumberStr.matches(classNumberRegex)) {
				log.error("invalid classNumber");
				throw new ValidationException("Invalid input");
			}
		}
	}

}
