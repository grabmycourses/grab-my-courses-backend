package open.seats.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "open.seats.tracker.repository")
@EnableEnversRepositories
@EnableTransactionManagement
public class ClassSeatsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassSeatsTrackerApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<RequestResponseLoggingFilter> registerReqResLogFilter() {
		FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RequestResponseLoggingFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("RequestResponseLoggingFilter");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}
