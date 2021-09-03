package open.seats.tracker.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.exception.ValidationException;
import open.seats.tracker.model.ConfirmationToken;
import open.seats.tracker.model.DropOption;
import open.seats.tracker.model.Notification;
import open.seats.tracker.model.Tracking;
import open.seats.tracker.model.User;
import open.seats.tracker.repository.ConfirmationTokensRepository;
import open.seats.tracker.repository.CoursesRepository;
import open.seats.tracker.repository.DropOptionsRepository;
import open.seats.tracker.repository.NotificationsRepository;
import open.seats.tracker.repository.TrackingsRepository;
import open.seats.tracker.repository.UsersRepository;
import open.seats.tracker.request.ForgotPasswordRequest;
import open.seats.tracker.request.ResetPasswordRequest;
import open.seats.tracker.request.SignUpRequest;
import open.seats.tracker.request.TrackingRequest;
import open.seats.tracker.response.CatalogCourseResponse;

@Log4j2
@Service("UserService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ConfirmationTokensRepository confirmationTokensRepository;

	@Autowired
	private DropOptionsRepository dropOptionsRepository;

	@Autowired
	private TrackingsRepository trackingsRepository;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private NotificationsRepository notificationsRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private Environment env;
	
	private static final String VERIFICATION_FAILURE_RESPONSE = "<!DOCTYPE html>\r\n"
			+ "<html lang=\"en\">\r\n"
			+ "  <head>\r\n"
			+ "    <meta charset=\"UTF-8\" />\r\n"
			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
			+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\r\n"
			+ "    <title>Grab My Courses</title>\r\n"
			+ "  </head>\r\n"
			+ "  <body>\r\n"
			+ "    <h4>\r\n"
			+ "      Something went wrong in verifying your account! Please drop a feedback <a href=\"https://www.grabmycourses.com/feedback\">here if the issue persists</a>.\r\n"
			+ "    </h4>\r\n"
			+ "  </body>\r\n"
			+ "</html>";
	
	private static final String VERIFICATION_FAILURE_DUPLICATE_RESPONSE = "<!DOCTYPE html>\r\n"
			+ "<html lang=\"en\">\r\n"
			+ "  <head>\r\n"
			+ "    <meta charset=\"UTF-8\" />\r\n"
			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
			+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\r\n"
			+ "    <title>Grab My Courses</title>\r\n"
			+ "  </head>\r\n"
			+ "  <body>\r\n"
			+ "    <h4>\r\n"
			+ "      Oh, looks like there is another account that has already been verified using your email. If this doesn't seem right, please <a href=\"https://www.grabmycourses.com/feedback\">drop us a feedback here</a>.\r\n"
			+ "    </h4>\r\n"
			+ "  </body>\r\n"
			+ "</html>";
	
	private static final String VERIFICATION_SUCCESS_RESPONSE = "<!DOCTYPE html>\r\n"
			+ "<html lang=\"en\">\r\n"
			+ "  <head>\r\n"
			+ "    <meta charset=\"UTF-8\" />\r\n"
			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
			+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\r\n"
			+ "    <title>Grab My Courses</title>\r\n"
			+ "  </head>\r\n"
			+ "  <body>\r\n"
			+ "    <h4>\r\n"
			+ "      Your account has been successfully verified now. Please click <a href=\"https://www.grabmycourses.com/login\">here to proceed to your account</a>.\r\n"
			+ "    </h4>\r\n"
			+ "  </body>\r\n"
			+ "</html>";

	@Override
	@Transactional
	public void signUp(SignUpRequest signUpRequest) throws ValidationException {

		if (usersRepository.existsByEmailAndVerifiedAndActive(signUpRequest.getEmail(), true, true)) {
			throw new ValidationException("An account already exists with this email.");
		}

		User newUser = new User();
		newUser.setFullName(signUpRequest.getFullName());
		newUser.setEmail(signUpRequest.getEmail());
		newUser.setPassword(encoder.encode(signUpRequest.getPassword()));
		newUser.setActive(true);
		newUser.setVerified(false);
		newUser.setCreatedTimestamp(Instant.now().toEpochMilli());
		newUser.setLastModifiedTimestamp(Instant.now().toEpochMilli());
		newUser = usersRepository.save(newUser);
		
		String token = UUID.randomUUID().toString();
		log.info("created new user with id:{} and email:{} and token:{}", newUser.getUserId(), newUser.getEmail(), token);
		
		confirmationTokensRepository.save(new ConfirmationToken(token, 1, newUser.getUserId(), Instant.now().toEpochMilli()));
		
		String redirectLink = env.getProperty("email.verification.click.link") + "?token="+token;
		notificationService.sendWelcomeEmail(newUser, redirectLink);
		
		Notification sentNotification = new Notification(1, newUser.getUserId(), 1, 0, Instant.now().toEpochMilli());
		notificationsRepository.save(sentNotification);
	}
	
	@Override
	@Transactional
	public String confirmAccountRegistration(String token) {
		
		try {
			ConfirmationToken confirmationToken = confirmationTokensRepository.findByTokenAndTypeAndActive(token, 1, true);
			if(confirmationToken==null) {
				return VERIFICATION_FAILURE_RESPONSE;
			}
			
			if (confirmationToken.getConfirmedAt() != null) {
	            return "Account already verified";
	        }

	        Long expiredAt = confirmationToken.getCreatedAt() + env.getProperty("verify.email.expiry.mins", Integer.class)*60000;
	        if (expiredAt < Instant.now().toEpochMilli()) {
	        	return "This verification link has expired already. Please try registering again and make sure that you verify the email as soon as possible.";
	        }
	        
	        User user = usersRepository.getById(confirmationToken.getUserId());
	        
	        // if some other account for same email has already been verified.
	        if(usersRepository.existsByEmailAndVerifiedAndActive(user.getEmail(), true, true)) {
	        	return VERIFICATION_FAILURE_DUPLICATE_RESPONSE;
	        }
	        
	        confirmationToken.setConfirmedAt(Instant.now().toEpochMilli());
	        confirmationTokensRepository.save(confirmationToken);
	        
	        user.setVerified(true);
	        usersRepository.save(user);
			return VERIFICATION_SUCCESS_RESPONSE;
		} catch (Exception e) {
			log.error("error in confirming user account", e);
			return VERIFICATION_FAILURE_RESPONSE;
		}
	}

	@Override
	public void addTracking(TrackingRequest trackingRequest) throws ValidationException {
		
		if(!usersRepository.existsById(trackingRequest.getUserId())) {
			throw new ValidationException("Woah, something sounds fishy here. Sorry, can't help you!");
		}

		List<CatalogCourseResponse> userCourses = getTracking(trackingRequest.getUserId());

		int maxActiveTrackingsAllowed = env.getProperty("max.active.trackings.allowed", Integer.class);
		if (userCourses.size() >= maxActiveTrackingsAllowed) {
			throw new ValidationException("You are allowed to have only maximum " + maxActiveTrackingsAllowed
					+ " active Trackings. Please remove an existing tracking if you want to add a new one.");
		}

		if (!coursesRepository.existsById(trackingRequest.getClassNumber())) { // if course doesn't exist in DB cache
			throw new ValidationException("Oops! Failed to add tracking for requested class.");
		}

		if (userCourses.stream()
				.anyMatch(existingCourse -> existingCourse.getClassNumber() == trackingRequest.getClassNumber())) {
			throw new ValidationException("Whoops! You already seem to be tracking this class");
		}

		List<DropOption> existingDropOptions = dropOptionsRepository.getByUserId(trackingRequest.getUserId());

		// prevent adding drop/swap option classes as a tracking.
		if (existingDropOptions.stream()
				.anyMatch(dropOption -> dropOption.getClassNumber() == trackingRequest.getClassNumber())) {
			throw new ValidationException(
					"Whoops! Failed to add tracking. You already seem to have this class added as a swap/drop option.");
		}

		trackingsRepository.save(new Tracking(trackingRequest.getUserId(), trackingRequest.getClassNumber()));
	}

	@Override
	public List<CatalogCourseResponse> getTracking(int userId) {
		return trackingsRepository.fetchCoursesTrackedByUser(userId);
	}

	@Override
	@Transactional
	public void removeTracking(int userId, List<Integer> classNumbersList) {
		for (int classNumber : classNumbersList) {
			trackingsRepository.deleteByUserIdAndClassNumberAndActive(userId, classNumber, true);
		}
	}

	@Override
	public void requestResetPassword(ForgotPasswordRequest request) throws ValidationException {
		//log.info("email given is:{}", request.getEmail());
		List<User> userEntries = usersRepository.findByEmailAndActiveOrderByCreatedTimestampDesc(request.getEmail(), true);
		if(CollectionUtils.isEmpty(userEntries)) {
			log.error("invalid emailId:{} for password reset", request.getEmail());
		} else {
			User mainAccount = userEntries.get(0);
			for(User thisUser : userEntries) {
				if(thisUser.isVerified()) {
					mainAccount = thisUser;
					break;
				}
			}
			
			if (BooleanUtils.isFalse(mainAccount.isVerified())) {
				throw new ValidationException(
						"This email account is not verified yet. Please either verify the existing account or register again with same email.");
			} else {
				//check if an unexpired OTP exists, resend same again
				ConfirmationToken existingOtp = confirmationTokensRepository.findTopByUserIdAndTypeOrderByCreatedAtDesc(mainAccount.getUserId(), 2);
				if(existingOtp!=null && existingOtp.getConfirmedAt()==null) {
					Long expiredAt = existingOtp.getCreatedAt() + env.getProperty("forgot.password.otp.expiry.seconds", Integer.class)*1000;
			        if (Instant.now().toEpochMilli() < expiredAt) {
			        	log.info("sending existing otp to user:{}", mainAccount.getUserId());
			        	notificationService.sendForgotPasswordOtpEmail(mainAccount, existingOtp.getToken());
						Notification sentNotification = new Notification(1, mainAccount.getUserId(), 5, 0, Instant.now().toEpochMilli());
						notificationsRepository.save(sentNotification);
						return;
			        }
				}
				
				// if no unexpired OTP exists, create new and send
				String otp = UUID.randomUUID().toString().substring(0, 6);
				if(confirmationTokensRepository.existsConfirmationTokenByTokenAndTypeAndActive(otp, 2, true)) {
					log.info("generated otp:{} already exists. Generating new otp now ...");
					otp = UUID.randomUUID().toString().substring(0, 6);
				} // ensure that generated OTP is unique
				confirmationTokensRepository.save(new ConfirmationToken(otp, 2, mainAccount.getUserId(), Instant.now().toEpochMilli()));
				
				notificationService.sendForgotPasswordOtpEmail(mainAccount, otp);
				Notification sentNotification = new Notification(1, mainAccount.getUserId(), 5, 0, Instant.now().toEpochMilli());
				notificationsRepository.save(sentNotification);
			}
		}
		
	}

	@Override
	public String resetPassword(ResetPasswordRequest request) throws ValidationException {
		ConfirmationToken otpToken = confirmationTokensRepository.findByTokenAndTypeAndActive(request.getOtp(), 2,
				true);
		if (otpToken == null) {
			log.error("invalid otp token");
			throw new ValidationException("Failed to verify OTP for reset password.");
		}

		if (otpToken.getConfirmedAt() != null) {
			throw new ValidationException("This OTP has already been used. Please request a fresh OTP.");
		}

		Long expiredAt = otpToken.getCreatedAt()
				+ env.getProperty("forgot.password.otp.expiry.seconds", Integer.class) * 1000;
		if (expiredAt < Instant.now().toEpochMilli()) {
			throw new ValidationException("This otp has already expired. Please request a fresh OTP.");
		}

		User user = usersRepository.getById(otpToken.getUserId());

		otpToken.setConfirmedAt(Instant.now().toEpochMilli());
		confirmationTokensRepository.save(otpToken);

		user.setPassword(encoder.encode(request.getNewPassword()));
		user.setLastModifiedTimestamp(Instant.now().toEpochMilli());
		usersRepository.save(user);
		return "Successfully resetted your password. You can now login with your new password.";
	}

}
