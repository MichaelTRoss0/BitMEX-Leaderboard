/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.dao;

import com.mtross.BitMEXLeaderboard.daoexceptions.PersistenceException;
import com.mtross.BitMEXLeaderboard.entity.Leaderboard;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author mike
 */
public class LeaderboardDaoFile implements LeaderboardDao {

    @Override
    public void loadFromFiles() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveToAllFiles() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Leaderboard addLeaderboard(Leaderboard leaderboard) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Leaderboard getLeaderboardByDate(LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Leaderboard> getAllLeaderboards() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLeaderboard(Leaderboard leaderboard) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteLeaderboard(LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
