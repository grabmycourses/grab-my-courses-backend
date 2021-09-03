package open.seats.tracker.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import open.seats.tracker.dto.SubjectNameDto;
import open.seats.tracker.model.Subject;
import open.seats.tracker.repository.SubjectsRepository;

@Component
public class DataUtils {
	
	@Autowired
	private SubjectsRepository programsRepository;
	
	private static Map<Integer, SubjectNameDto> subjectsCache = null;
	private static Map<String, Integer> subjectCodeToIdMap = null;
	public static final List<String> sessionsList = Arrays.asList("A", "B", "C", "DYN"); 
	public static int asuFetchPagesSleepDelayInSeconds = 0; 
	public static int levelwiseSubjectsBatchingWaitInSeconds = 0; 
	public static final String OPEN_CLASS_ALERT_EMAIL_SUBJECT = " is now open. Grab it fast!";
	
	public Map<Integer, SubjectNameDto> getSubjectsData() {
		if (subjectsCache != null) {
			return subjectsCache;
		}
		List<Subject> programsList = programsRepository.findAll();
		subjectsCache = programsList.stream().collect(Collectors.toMap(Subject::getSubjectId,
				s -> new SubjectNameDto(s.getSubjectCode(), s.getSubjectName())));
		return subjectsCache;
	}
	
	public Map<String, Integer> getSubjectsIdForSubjectCode() {
		if (subjectCodeToIdMap != null) {
			return subjectCodeToIdMap;
		}
		List<Subject> programsList = programsRepository.findAll();
		subjectCodeToIdMap = programsList.stream().collect(Collectors.toMap(Subject::getSubjectCode, Subject::getSubjectId));
		return subjectCodeToIdMap;
	}
	
	public void refreshCache(String prefix) {
		if (StringUtils.isNotBlank(prefix) && prefix.equalsIgnoreCase("subjects")) {
			subjectsCache = null;
			subjectCodeToIdMap = null;
		}  
	}
}
