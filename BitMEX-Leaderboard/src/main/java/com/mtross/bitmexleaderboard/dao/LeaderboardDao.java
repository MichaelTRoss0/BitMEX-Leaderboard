/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.daoexceptions.PersistenceException;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mike
 */
public interface LeaderboardDao {

    public void loadFromFiles()
            throws PersistenceException;

    public void saveToAllFiles()
            throws PersistenceException;

    public void makeUserMap(Map<String, User> mapOfUsers);

    public Leaderboard addLeaderboard(Leaderboard leaderboard);

    public Leaderboard getLeaderboardByDate(LocalDate date);

    public List<Leaderboard> getAllLeaderboards();

    public void updateLeaderboard(LocalDate date, Leaderboard leaderboard);

    public void deleteLeaderboard(LocalDate date);

    public Leaderboard makeTodaysLeaderboard();

}
