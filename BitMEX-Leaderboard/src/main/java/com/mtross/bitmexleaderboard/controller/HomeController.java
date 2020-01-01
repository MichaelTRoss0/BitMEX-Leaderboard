/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.controller;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.service.LeaderboardManagerImpl;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author mike
 */
@Controller
public class HomeController {

    @Autowired
    private LeaderboardManagerImpl service;
    
//    private final Clock utcClock = Clock.systemUTC();
//    private final ZoneOffset utcOffset = ZoneOffset.UTC;
//    private final ZoneId utc = ZoneId.of("Z");

    @GetMapping("")
    public String getIndex(Model model) {
        return getLeaderboards(model);
    }

    @GetMapping("/home")
    public String getLeaderboards(Model model) {
        Leaderboard leaderboardTDY = new Leaderboard();
        Leaderboard leaderboardYDA = new Leaderboard();
        
        
        
        model.addAttribute("leaderboardTDY", leaderboardTDY);
        model.addAttribute("leaderboardYDA", leaderboardYDA);

        return "home";
    }

}
