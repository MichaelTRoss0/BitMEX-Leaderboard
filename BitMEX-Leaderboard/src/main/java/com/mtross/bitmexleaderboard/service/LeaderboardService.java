/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.service;

import com.mtross.bitmexleaderboard.dao.LeaderboardRepository;
import com.mtross.bitmexleaderboard.dao.UserRepository;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mike
 */
@Service
public class LeaderboardService {

    @Autowired
    UserRepository user;

    @Autowired
    LeaderboardRepository leaderboard;

    public Leaderboard findLeaderboardByDate(LocalDate date) {
        return leaderboard.findByDate(date);
    }

    public Set<Leaderboard> findAllLeaderboards() {
        return (Set<Leaderboard>) leaderboard.findAll();
    }

    public User findUserByUsername(String username) {
        return user.findByUsername(username);
    }

    public Set<User> findAllUsers() {
        return (Set<User>) user.findAll();
    }

}
