package com.MonitoringTool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoringToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringToolApplication.class, args);
	}

}
