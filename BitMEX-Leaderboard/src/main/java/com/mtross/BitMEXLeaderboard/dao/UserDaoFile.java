/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.dao;

import com.mtross.BitMEXLeaderboard.daoexceptions.PersistenceException;
import com.mtross.BitMEXLeaderboard.entity.User;
import java.util.List;

/**
 *
 * @author mike
 */
public class UserDaoFile implements UserDao {

    @Override
    public void loadFromFiles() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveToAllFiles() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User addUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserByName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserByRank(int rank) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
