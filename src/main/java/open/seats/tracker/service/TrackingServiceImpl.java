package open.seats.tracker.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.dto.ValueLabelDto;
import open.seats.tracker.model.Course;
import open.seats.tracker.repository.CoursesRepository;
import open.seats.tracker.request.SearchCatalogRequest;
import open.seats.tracker.response.CatalogCourseResponse;
import open.seats.tracker.util.DataUtils;

@Log4j2
@Service("TrackingService")
public class TrackingServiceImpl implements TrackingService {

	@Autowired
	private DataUtils dataUtils;

	@Autowired
	private CoursesRepository coursesRepository;

	@Override
	public List<CatalogCourseResponse> searchCatalog(SearchCatalogRequest request) {

		// performing exact search in DB
		List<CatalogCourseResponse> catalogSearchResponse = null;
		if (StringUtils.isNotBlank(request.getSession())) {
			catalogSearchResponse = coursesRepository.fetchCoursesWithSession(request.getSubjectId(),
					request.getCourseNumber().replace('*', '_'), request.getSession(), request.isOnlineCourse());
		} else {
			catalogSearchResponse = coursesRepository.fetchCoursesWithoutSession(request.getSubjectId(),
					request.getCourseNumber().replace('*', '_'), request.isOnlineCourse());
		}

		if (CollectionUtils.isNotEmpty(catalogSearchResponse)) {
			return catalogSearchResponse;
		}

		int countInLevel = coursesRepository.getCountInCourseLevel(request.getSubjectId(),
				request.getCourseNumber().substring(0, 1) + "**", request.isOnlineCourse());
		log.info("count of {} level courses is:{}", request.getCourseNumber().substring(0, 1) + "**", countInLevel);

		if (countInLevel > 0) {
			return catalogSearchResponse; // it is empty anyway
		}

		/*
		 * Logic for loading general catalog list is not available in public because of privacy policies
		 */

		if (CollectionUtils.isNotEmpty(coursesList)) {
			coursesRepository.saveAllAndFlush(coursesList);
		}

		return getFilteredSearchCatalogResponse(coursesList, request);
	}

	private List<CatalogCourseResponse> getFilteredSearchCatalogResponse(List<Course> coursesList,
			SearchCatalogRequest request) {
		List<CatalogCourseResponse> responseList = new ArrayList<>();
		for (Course catalogCourse : coursesList) {
			if (catalogCourse.getCourseNumber().matches(request.getCourseNumber().replace('*', '.'))
					&& (StringUtils.isBlank(request.getSession())
							|| StringUtils.equals(catalogCourse.getSession(), request.getSession()))) {
				CatalogCourseResponse responseCourse = new CatalogCourseResponse();
				responseCourse.setClassNumber(catalogCourse.getClassNumber());
				responseCourse.setClassTitle(catalogCourse.getClassTitle());
				responseCourse.setCourse(dataUtils.getSubjectsData().get(catalogCourse.getSubjectId()).getSubjectCode()
						+ " " + catalogCourse.getCourseNumber());
				responseCourse.setInstructor(catalogCourse.getInstructor());
				responseCourse.setSession(catalogCourse.getSession());
				responseList.add(responseCourse);
			}
		}
		return responseList;
	}

	@Override
	public List<ValueLabelDto> getSubjectsData() {
		return dataUtils.getSubjectsData().entrySet().stream()
				.map(entry -> new ValueLabelDto(entry.getKey().toString(),
						entry.getValue().getSubjectCode() + " - " + entry.getValue().getSubjectName()))
				.collect(Collectors.toList());
	}

}
