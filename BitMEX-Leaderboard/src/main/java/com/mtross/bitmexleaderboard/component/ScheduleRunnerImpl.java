package com.mtross.bitmexleaderboard.component;

import java.io.IOException;
import java.net.ProtocolException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.service.LeaderboardManagerImpl;

/**
 *
 * @author mike
 */
@Component
public class ScheduleRunnerImpl {

	@Autowired
	private LeaderboardManagerImpl manager;

	@Scheduled(cron = "0 5 12 * * ?", zone = "UTC")
	public void scheduledDataCollection() throws ProtocolException, IOException {
		Leaderboard leaderboard = manager.makeLeaderboard();
		manager.mergeLeaderboardIntoDatabase(leaderboard);
	}

}
