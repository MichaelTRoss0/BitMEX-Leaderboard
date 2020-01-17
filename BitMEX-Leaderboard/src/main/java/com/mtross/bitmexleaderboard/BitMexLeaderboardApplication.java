package com.mtross.bitmexleaderboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BitMexLeaderboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitMexLeaderboardApplication.class, args);
	}

}
