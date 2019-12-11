/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.daoexceptions.PersistenceException;
import com.mtross.bitmexleaderboard.entity.User;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mike
 */
@Repository
public class UserDaoFile implements UserDao {

    public static String USER_FILE;
    public static final String DELIMITER = ",";

    public UserDaoFile() {
        USER_FILE = "Data/User_name.txt";
    }

    public UserDaoFile(String filename) {
        USER_FILE = filename;
    }

    private Map<User, File> userFiles = new HashMap<>();
    private Map<String, User> userMap = new HashMap<>();

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

    @Override
    public HashMap<String, User> getUserMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
