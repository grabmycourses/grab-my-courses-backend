package open.seats.tracker.service;

import java.util.List;

import open.seats.tracker.dto.ValueLabelDto;
import open.seats.tracker.request.SearchCatalogRequest;
import open.seats.tracker.response.CatalogCourseResponse;

public interface TrackingService {
	List<CatalogCourseResponse> searchCatalog(SearchCatalogRequest request);

	List<ValueLabelDto> getSubjectsData();
}
