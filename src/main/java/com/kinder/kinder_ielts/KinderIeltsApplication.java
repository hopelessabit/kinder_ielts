package com.kinder.kinder_ielts;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class KinderIeltsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KinderIeltsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set the default JVM time zone to UTC+7
		TimeZone.setDefault(TimeZone.getTimeZone("UTC+7"));
	}
}
