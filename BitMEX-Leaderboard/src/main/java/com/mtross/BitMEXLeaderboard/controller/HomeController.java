/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.controller;

import com.mtross.BitMEXLeaderboard.dao.LeaderboardDao;
import com.mtross.BitMEXLeaderboard.dao.UserDao;
import java.time.LocalDateTime;
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
    UserDao userDao;

    @Autowired
    LeaderboardDao leaderboardDao;

    @GetMapping("")
    public String getIndex(Model model) {
        return checkLeaderboard(model);
    }

    @GetMapping("/home")
    public String checkLeaderboard(Model model) {
        LocalDateTime now = LocalDateTime.now();
        
        // TODO - fill out this program after finishing the DAOs and service layers

        return "home";
    }

}
