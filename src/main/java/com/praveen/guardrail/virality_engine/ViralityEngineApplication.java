package com.praveen.guardrail.virality_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class ViralityEngineApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(ViralityEngineApplication.class, args);
	}

}
