package open.seats.tracker.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.dto.DropOptionsDto;
import open.seats.tracker.dto.SwapPreferencesDto;
import open.seats.tracker.dto.ValueLabelDto;
import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.model.DropOption;
import open.seats.tracker.model.User;
import open.seats.tracker.repository.DropOptionsRepository;
import open.seats.tracker.repository.UsersRepository;
import open.seats.tracker.request.AddDropOptionRequest;
import open.seats.tracker.request.RemoveDropOptionsRequest;
import open.seats.tracker.request.UpdatePrivacyRequest;
import open.seats.tracker.response.CatalogCourseResponse;
import open.seats.tracker.response.DropOptionsResponse;
import open.seats.tracker.response.SwapPreferencesResponse;
import open.seats.tracker.util.DataUtils;

@Log4j2
@Service("SwappingService")
public class SwappingServiceImpl implements SwappingService {

	@Autowired
	private DropOptionsRepository dropOptionsRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private DataUtils dataUtils;

	@Autowired
	private Environment env;

	@Override
	@Transactional
	public SwapPreferencesResponse addUserDropOption(AddDropOptionRequest request) throws ValidationException {

		List<DropOption> dropOptions = dropOptionsRepository.getByUserId(request.getUserId());
		log.info("user {} has {} drop otpions currently.", request.getUserId(), dropOptions.size());

		int toBeAddedDropOption = Integer.parseInt(request.getDropOption());

		int maxActiveSwapOptionsAllowed = env.getProperty("max.active.swap.options.allowed", Integer.class);
		if (dropOptions.size() >= maxActiveSwapOptionsAllowed) {
			throw new ValidationException(
					"Whoops! Failed to add swap/drop preferences. You are allowed to have only maximum "
							+ maxActiveSwapOptionsAllowed
							+ " active swap/drop preferences. Please remove an existing class swap preference if you want to add a new one.");
		}

		if (dropOptions.stream().anyMatch(dropOption -> dropOption.getClassNumber() == toBeAddedDropOption)) {
			throw new ValidationException("Whoops! You already seem to have this class added as a swap/drop option.");
		}

		List<CatalogCourseResponse> userExistingTrackings = userService.getTracking(request.getUserId());

		// prevent adding tracked classes as a swap option.
		if (userExistingTrackings.stream()
				.anyMatch(userTrackedClass -> userTrackedClass.getClassNumber() == toBeAddedDropOption)) {
			throw new ValidationException(
					"Whoops! Failed to add swap/drop preferences. You can't add a class that you are already tracking, as a swap/drop option.");
		}

		dropOptionsRepository.saveAndFlush(new DropOption(request.getUserId(), toBeAddedDropOption));

		dropOptions.add(new DropOption(request.getUserId(), toBeAddedDropOption));
		return fetchUserSwapOptions(request.getUserId(), dropOptions);
	}

	@Override
	@Transactional
	public SwapPreferencesResponse removeUserDropOptions(RemoveDropOptionsRequest request) throws ValidationException {
		for (String classNum : request.getDropOptions()) {
			dropOptionsRepository.deleteByUserIdAndClassNumberAndActive(request.getUserId(), Integer.parseInt(classNum),
					true);
		}

		return fetchUserSwapOptions(request.getUserId(), null);
	}

	@Override
	public SwapPreferencesResponse fetchUserSwapOptions(int userId, List<DropOption> existingDropOptions)
			throws ValidationException {
		try {
			User thisUser = usersRepository.getById(userId);

			if (thisUser.isDropSharing()) {
				List<SwapPreferencesDto> swapOptions = dropOptionsRepository.getUserSwapOptions(userId);

				List<DropOptionsDto> swapOptionsList = new ArrayList<>();

				for (SwapPreferencesDto swapOrDropOption : swapOptions) {

					if (CollectionUtils.isEmpty(swapOptionsList) || swapOptionsList.get(swapOptionsList.size() - 1)
							.getTrackedClassNumber() != swapOrDropOption.getTrackingClassNumber()) {
						swapOptionsList
								.add(new DropOptionsDto(swapOrDropOption.getTrackingClassNumber(), new ArrayList<>()));
					} // this ensures that this tracked class entry is present at end of list

					if (swapOrDropOption.getSwappingClassNumber() != null) { // swapping entry found
						String thisOption = dataUtils.getSubjectsData().get(swapOrDropOption.getSwappingSubjectId())
								.getSubjectCode() + " " + swapOrDropOption.getSwappingCourseNumber() + "(#"
								+ swapOrDropOption.getSwappingClassNumber() + ")";
						boolean priorEntryFound = false; // to check if there was some other option earlier added for
															// this swapping user
						for (DropOptionsResponse priorUserEntry : swapOptionsList.get(swapOptionsList.size() - 1)
								.getDropDetails()) {
							if (priorUserEntry.getDropperEmail().equals(swapOrDropOption.getSwappingUserEmail())) {
								// prior entry found for user in the options list, just append this option
								priorUserEntry.setDropperDropOptions(
										priorUserEntry.getDropperDropOptions() + " OR " + thisOption);
								priorEntryFound = true;
								break;
							}
						}
						if (!priorEntryFound) {
							swapOptionsList.get(swapOptionsList.size() - 1).getDropDetails()
									.add(new DropOptionsResponse(swapOrDropOption.getSwappingUserEmail(), thisOption));
						}

					} else { // drop only entry found
						swapOptionsList.get(swapOptionsList.size() - 1).getDropDetails()
								.add(new DropOptionsResponse(swapOrDropOption.getSwappingUserEmail(), ""));
					}

				}

				if (existingDropOptions == null) {
					existingDropOptions = dropOptionsRepository.getByUserId(userId);
				}
				List<ValueLabelDto> dropOptions = existingDropOptions.stream()
						.map(dropOption -> new ValueLabelDto(String.valueOf(dropOption.getClassNumber()),
								String.valueOf(dropOption.getClassNumber())))
						.collect(Collectors.toList());

				return new SwapPreferencesResponse(true, swapOptionsList, dropOptions);
			} else {
				return new SwapPreferencesResponse(false);
			}
		} catch (EntityNotFoundException e) {
			log.error("error: user not found");
			throw new ValidationException("Woah, something sounds fishy here. Sorry, can't help you!");
		}

	}

	@Override
	@Transactional
	public SwapPreferencesResponse updatePrivacySetting(UpdatePrivacyRequest request) throws ValidationException {
		try {
			User user = usersRepository.getById(request.getUserId());
			user.setDropSharing(request.isSharingEnabled());
			user.setLastModifiedTimestamp(Instant.now().toEpochMilli());
			usersRepository.save(user);

			if (request.isSharingEnabled()) {
				return fetchUserSwapOptions(request.getUserId(), null);
			} else {
				return null;
			}
		} catch (EntityNotFoundException e) {
			log.error("error: user not found");
		}
		return null;
	}

}
