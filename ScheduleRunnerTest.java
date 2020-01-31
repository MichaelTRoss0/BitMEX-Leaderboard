package com.mtross.BitMEXLeaderboard.component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.junit4.SpringRunner;

import com.mtross.bitmexleaderboard.component.ScheduleRunnerImpl;

/**
 *
 * @author mike
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleRunnerTest {

	@Autowired
	ScheduleRunnerImpl scheduleRunner;

	public ScheduleRunnerTest() {
	}

	@BeforeAll
	public static void setUpClass() {
	}

	@AfterAll
	public static void tearDownClass() {
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	// Testing to see that the cron expression of method scheduledDataCollection works as intended
	@Test
	public void testScheduledDataCollectionCron() {
		//(cron = "0 5 12 * * ?", zone = "UTC")
		org.springframework.scheduling.support.CronTrigger trigger = 
				new CronTrigger("0 5 12 * * ?");

		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone("UTC"));
		today.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 12, 5, 0);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE"); 
	    final Date yesterday = today.getTime();
		log.info("Yesterday was : " + df.format(yesterday));

	}

}
