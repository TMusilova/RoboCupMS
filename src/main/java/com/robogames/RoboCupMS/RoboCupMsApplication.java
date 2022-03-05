package com.robogames.RoboCupMS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoboCupMsApplication {

	private static final Logger logger = LoggerFactory.getLogger(RoboCupMsApplication.class);

	public static Logger getLogger() {
		return RoboCupMsApplication.logger;
	}

	public static void main(String[] args) {
		SpringApplication.run(RoboCupMsApplication.class, args);
	}

}
