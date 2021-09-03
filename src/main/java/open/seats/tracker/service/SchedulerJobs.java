package open.seats.tracker.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.dto.SubjectLevelDto;
import open.seats.tracker.dto.UserTrackedClassesDto;
import open.seats.tracker.model.Notification;
import open.seats.tracker.model.User;
import open.seats.tracker.repository.CoursesRepository;
import open.seats.tracker.repository.NotificationsRepository;
import open.seats.tracker.util.DataUtils;

@Configuration
@EnableScheduling
@Log4j2
public class SchedulerJobs {

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private ClassSearchUtil classSearchUtil;

	@Autowired
	private Environment env;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private NotificationsRepository notificationsRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private DataUtils dataUtils;

	@Scheduled(fixedRateString = "${open.seats.alert.scheduler.delay}")
	public void sendOpenSeatsAlert() {
		if (BooleanUtils.isTrue(env.getProperty("SCHEDULER_STATUS", Boolean.class, true))) {
			long startTime = System.currentTimeMillis();
			log.info("*** sendOpenSeatsAlert scheduler job running ");

			List<UserTrackedClassesDto> trackedClassesForUsers = coursesRepository.getTrackedClassesForUsers();
			if (CollectionUtils.isEmpty(trackedClassesForUsers)) { // if no classes being tracked yet
				return;
			}

			/*
			 * below transformation converts above list into map of classNumber key vs list
			 * of user tracking that class
			 */
			Map<Integer, List<User>> classNumberVsTrackingUsers = CollectionUtils.emptyIfNull(trackedClassesForUsers)
					.stream().collect(Collectors.groupingBy(UserTrackedClassesDto::getClassNumber,
							Collectors.mapping(UserTrackedClassesDto::getUser, Collectors.toList())));

			List<SubjectLevelDto> levelsAndSubjects = coursesRepository.getLevelwiseTrackedInPersonSubjects();

			/*
			 * below transformation converts above list into map of courselevel vs
			 * corresponding comma separated tracked subjects
			 */
			Map<String, String> trackedSubjects = CollectionUtils.emptyIfNull(levelsAndSubjects).stream()
					.collect(Collectors.groupingBy(SubjectLevelDto::getCourseLevel,
							Collectors.mapping(SubjectLevelDto::getSubjectCode, Collectors.joining(","))));
			log.info("levelwiseSubjects map is:{}", trackedSubjects);

			/*
			 * Some logic for open class notifications is not available in public because of privacy policies.
			 */

			try {
				int waitingPeriodInSeconds = env.getProperty("subjects.batching.wait.seconds", Integer.class, 3);
				log.info("*** Waiting for {} seconds now before syncing next group of subjects",
						waitingPeriodInSeconds);
				TimeUnit.SECONDS.sleep(waitingPeriodInSeconds);
			} catch (InterruptedException e) {
				log.error("InterruptedException in sleep in sync logic", e);
			}

			long endTime = System.currentTimeMillis();
			log.info(">>> time taken in this course sync:{} seconds", (endTime - startTime) / 1000);
		}
	}

	private void notifyUsersAboutOpenClass(String courseCode, int classNumber, String classTitle,
			List<User> usersToBeNotified) {

		courseCode = courseCode + " (#" + classNumber + ")";

		for (User user : usersToBeNotified) {
			// remove user's tracking for this class after notifying once
			if (env.getProperty("remove.tracking.after.notification", Boolean.class, true)) {
				log.info("notifying user:{} about open class {} titled {}", user.getUserId(), courseCode, classTitle);
				emailUsersAboutOpenClass(courseCode, classTitle, user);
				// save notification sent info in DB
				Notification sentNotification = new Notification(1, user.getUserId(), 3, classNumber,
						Instant.now().toEpochMilli());
				notificationsRepository.save(sentNotification);
				userService.removeTracking(user.getUserId(), Arrays.asList(classNumber));
			} else { // case when the above flag is turned off, follow gap strategy
				// get last sent notification details for this user, if any
				Notification lastNotification = notificationsRepository
						.findFirstByUserIdAndClassNumberAndModeAndTypeOrderByTimestampDesc(user.getUserId(),
								classNumber, 1, 3);
				int duplicateNotificationDealyInSec = env.getProperty("duplicate.open.seat.notification.wait.seconds",
						Integer.class);
				long currentTimestamp = Instant.now().toEpochMilli();
				/*
				 * if no earlier notfication or last one was before duplicate wait period, then
				 * notify now, else skip this user
				 */
				if (lastNotification == null || (currentTimestamp - lastNotification.getTimestamp())
						/ 1000 > duplicateNotificationDealyInSec) {
					log.info("notifying user:{} about open class {} titled {}", user.getUserId(), courseCode,
							classTitle);
					emailUsersAboutOpenClass(courseCode, classTitle, user);
					// save notification sent info in DB
					Notification sentNotification = new Notification(1, user.getUserId(), 3, classNumber,
							Instant.now().toEpochMilli());
					notificationsRepository.save(sentNotification);
				} else {
					log.info("skipping open seat notification for user:{} and class:{}; last one sent at:{}",
							user.getUserId(), classNumber, lastNotification.getTimestamp());
				}
			}

			// wait for configured time before sending email to next user; SES limitation
			try {
				int waitingPeriodInSeconds = env.getProperty("delay.between.emails.millis", Integer.class, 1000);
				log.info("*** Waiting for {} milliseconds now before emailing next user", waitingPeriodInSeconds);
				TimeUnit.MILLISECONDS.sleep(waitingPeriodInSeconds);
			} catch (InterruptedException e) {
				log.error("InterruptedException in sleep in sending email wait", e);
			}
		}
	}

	public void emailUsersAboutOpenClass(String courseCode, String classTitle, User user) {
		String body = "Hi " + user.getFullName() + ",\nYour long awaited course " + courseCode + " - " + classTitle
				+ " is now open for registration again.\n\nGo grab it now. It will run out sooner than you can imagine."
				+ "\n\nIMPORTANT NOTE: To avoid spamming you with too many emails, we send the email notification alert for open seat "
				+ "in your tracked course ONLY ONCE. \nHence, once you recieve this email, you are automatically removed from tracking this class any further. "
				+ "However, if for some reason you failed to grab this open seat - you can always go back to your Grab My Courses account and "
				+ "add tracking for this class again. We are parallely working to make this setting available as a customizable preference "
				+ "in your account.\n\nCheers!\nNaveen from Grab My Courses";
		notificationService.sendEmail("open.seat", user.getEmail(),
				courseCode + dataUtils.OPEN_CLASS_ALERT_EMAIL_SUBJECT, body, false);
	}

}
