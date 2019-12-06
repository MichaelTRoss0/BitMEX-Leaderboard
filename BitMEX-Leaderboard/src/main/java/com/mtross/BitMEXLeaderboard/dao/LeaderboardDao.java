/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.dao;

import com.mtross.BitMEXLeaderboard.entity.Leaderboard;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mike
 */
public interface LeaderboardDao {

    public Leaderboard addLeaderboard(Leaderboard leaderboard);

    public Leaderboard getLeaderboardByDate(LocalDate date);

    public List<Leaderboard> getAllLeaderboards();

    public void updateLeaderboard(Leaderboard leaderboard);

    public void deleteLeaderboard(LocalDate date);

}
