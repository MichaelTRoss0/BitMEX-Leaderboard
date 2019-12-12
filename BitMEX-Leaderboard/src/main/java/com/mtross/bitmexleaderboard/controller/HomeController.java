/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.controller;

import com.mtross.bitmexleaderboard.dao.LeaderboardRepository;
import com.mtross.bitmexleaderboard.dao.UserRepository;
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
    UserRepository user;

    @Autowired
    LeaderboardRepository leaderboard;

    @GetMapping("")
    public String getIndex(Model model) {
        return displayChangesInProfit(model);
    }

    @GetMapping("/home")
    public String displayChangesInProfit(Model model) {

        return "home";
    }

}
