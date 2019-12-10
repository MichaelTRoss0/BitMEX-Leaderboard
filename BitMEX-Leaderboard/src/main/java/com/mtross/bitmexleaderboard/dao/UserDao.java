/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.daoexceptions.PersistenceException;
import com.mtross.bitmexleaderboard.entity.User;
import java.util.List;

/**
 *
 * @author mike
 */
public interface UserDao {

    public void loadFromFiles()
            throws PersistenceException;

    public void saveToAllFiles()
            throws PersistenceException;

    public User addUser(User user);

    public User getUserByName(String name);

    public User getUserByRank(int rank);

    public List<User> getAllUsers();

    public void updateUser(User user);

    public void deleteUser(User user);

}
