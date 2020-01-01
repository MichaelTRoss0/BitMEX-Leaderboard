/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mike
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {


//    @Query(value = "SELECT * FROM `User` u "
//            + "INNER JOIN User_History uh "
//            + "ON uh.user_id = u.user_id "
//            + "WHERE u.username = ?1",
//            nativeQuery = true)
    public User findByUsername(String username);

}
