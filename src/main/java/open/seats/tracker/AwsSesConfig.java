package open.seats.tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AwsSesConfig {

	@Bean
	public AmazonSimpleEmailService amazonSimpleEmailService() {
		return AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
	}

	/*
	 * JavaMailSender bean is automatically created by spring-boot-starter-mail
	 * dependency using corresponding application.properties values
	 */
}
