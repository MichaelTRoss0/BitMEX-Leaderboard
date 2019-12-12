/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mike
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    public User findByUsername(String username);
    
}
