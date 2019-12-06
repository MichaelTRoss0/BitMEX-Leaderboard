/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.dao;

import com.mtross.BitMEXLeaderboard.entity.User;
import java.util.List;

/**
 *
 * @author mike
 */
public interface UserDao {

    public User addUser(User user);

    public User getUserByName(String name);

    public User getUserByRank(int rank);

    public List<User> getAllUsers();

    public void updateUser(User user);

    public void deleteUser(User user);

}
