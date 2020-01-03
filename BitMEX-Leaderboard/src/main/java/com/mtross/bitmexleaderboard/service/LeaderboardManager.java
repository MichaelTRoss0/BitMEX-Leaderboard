/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.service;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.io.IOException;
import java.net.ProtocolException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mike
 */
//@Service
public interface LeaderboardManager {

    public Leaderboard findLeaderboardByDate(LocalDate date);

    public List<Leaderboard> findLeaderboardByDateBetween(LocalDate startDate, LocalDate stopDate);

    public List<Leaderboard> findAllLeaderboards();

    public Leaderboard saveLeaderboard(Leaderboard leaderboard);

    public List<Leaderboard> saveAllLeaderboards(List<Leaderboard> leaderboards);

    public void deleteLeaderboardById(Integer id);

    public void deleteAllLeaderboards();

    public void flushLeaderboards();

    public User findUserByUsername(String username);

    public List<User> findAllUsers();

    public User saveUser(User user);

    public List<User> saveAllUsers(List<User> users);

    public void deleteUserById(Integer id);

    public void deleteAllUsers();

    public void flushUsers();

    public Leaderboard makeLeaderboard() throws ProtocolException, IOException;

    public void mergeLeaderboardIntoDatabase(Leaderboard leaderboard);

    public List<List<String>> createDifferenceTable(Leaderboard lbOld, Leaderboard lbNew);

}
