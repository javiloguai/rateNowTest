package com.berge.ratenow.testapplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jruizh
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class RateNowTestApplication extends SpringBootServletInitializer {

	private static final Logger LOGGER = LogManager.getLogger(RateNowTestApplication.class);

	public static void main(String[] args) {
		System.setProperty("-DLog4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		SpringApplication.run(RateNowTestApplication.class, args);

		LOGGER.info("RateNowTestApplication Starting");

	}

	/**
	 * @param builder SpringApplicationBuilder
	 * @return SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RateNowTestApplication.class);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
