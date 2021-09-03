package open.seats.tracker.service;

import java.util.List;

import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.model.DropOption;
import open.seats.tracker.request.AddDropOptionRequest;
import open.seats.tracker.request.RemoveDropOptionsRequest;
import open.seats.tracker.request.UpdatePrivacyRequest;
import open.seats.tracker.response.SwapPreferencesResponse;

public interface SwappingService {

	SwapPreferencesResponse addUserDropOption(AddDropOptionRequest request) throws ValidationException;

	SwapPreferencesResponse removeUserDropOptions(RemoveDropOptionsRequest request) throws ValidationException;

	SwapPreferencesResponse fetchUserSwapOptions(int userId, List<DropOption> existingDropOptions) throws ValidationException;

	SwapPreferencesResponse updatePrivacySetting(UpdatePrivacyRequest request) throws ValidationException;

}
