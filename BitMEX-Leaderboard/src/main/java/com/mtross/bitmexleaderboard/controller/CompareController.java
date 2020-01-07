/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.controller;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.service.LeaderboardManagerImpl;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author mike
 */
@Controller
public class CompareController {

    @Autowired
    private LeaderboardManagerImpl manager;
    
    @GetMapping("/compare")
    public String compareLeaderboards(HttpServletRequest request, Model model) {
        Leaderboard leaderboardOld;
        Leaderboard leaderboardNew;

        List<List<String>> differenceTable;

        LocalDate startDate = LocalDate.parse(request.getParameter(""));
        LocalDate stopDate = LocalDate.parse(request.getParameter(""));

        leaderboardOld = manager.findLeaderboardByDate(startDate);
        leaderboardNew = manager.findLeaderboardByDate(stopDate);

        differenceTable = manager.buildDifferenceTable(leaderboardOld, leaderboardNew);

        model.addAttribute("stopDate", stopDate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("differenceTable", differenceTable);
        
        return "compare";
    }
    
}
