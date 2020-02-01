/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.controller;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.service.LeaderboardManagerImpl;

/**
 *
 * @author mike
 */
@Controller
public class HomeController {

	@Autowired
	private LeaderboardManagerImpl manager;

	private final Clock UTC_CLOCK = Clock.systemUTC();

	@GetMapping("")
	public String getIndex(Model model) {
		return homeStart(model);
	}

	@GetMapping("/home")
	public String homeStart(Model model) {
		Leaderboard leaderboardYDA;
		Leaderboard leaderboardTDY;

		List<List<String>> differenceTable;

		LocalDate today = findDateForToday();
		LocalDate yesterday = today.minusDays(1);

		leaderboardYDA = manager.findLeaderboardByDate(yesterday);
		leaderboardTDY = manager.findLeaderboardByDate(today);

		differenceTable = manager.buildDifferenceTable(leaderboardYDA, leaderboardTDY);

		model.addAttribute("today", today);
		model.addAttribute("yesterday", yesterday);
		model.addAttribute("differenceTable", differenceTable);

		return "home";
	}

	private LocalDate findDateForToday() {
		LocalDateTime now = LocalDateTime.now(UTC_CLOCK);

		int hour = now.getHour();
		int minute = now.getMinute();
		if (hour > 12 || (hour == 12 && minute >= 30)) {
			return LocalDate.now(UTC_CLOCK);
		} else {
			return LocalDate.now(UTC_CLOCK).minusDays(1);
		}
	}

}
