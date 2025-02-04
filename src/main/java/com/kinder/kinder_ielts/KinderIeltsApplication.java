package com.kinder.kinder_ielts;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Map;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class KinderIeltsApplication {

	private static final Logger logger = LoggerFactory.getLogger(KinderIeltsApplication.class);
	public static void main(String[] args) {

		logger.info("Logging Environment Variables:");
		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			logger.info("  {}: {}", envName, env.get(envName));
		}
		logger.info("End of Environment Variables.");

		SpringApplication.run(KinderIeltsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set the default JVM time zone to UTC+7
		TimeZone.setDefault(TimeZone.getTimeZone("UTC+7"));
	}
}
