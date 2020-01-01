/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.dao;

import com.mtross.BitMEXLeaderboard.Generator;
import com.mtross.bitmexleaderboard.dao.LeaderboardRepository;
import com.mtross.bitmexleaderboard.dao.UserRepository;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mike
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    private List<User> TEST_USERS;
    private List<Leaderboard> TEST_LEADERBOARDS;

    public UserRepositoryTest() {
        TEST_USERS = Generator.generateTDUsers();
        TEST_LEADERBOARDS = Generator.generateTDLeaderboards(TEST_USERS);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        leaderboardRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.saveAll(TEST_USERS);
        TEST_USERS = userRepository.findAll();

        leaderboardRepository.saveAll(TEST_LEADERBOARDS);
        TEST_LEADERBOARDS = leaderboardRepository.findAll();
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of findByUsername method, of class UserRepository.
     */
    @Test
    @Transactional
    public void testFindByUsername() {
        for (User user : TEST_USERS) {
            String username = user.getUsername();
            User fromDB = userRepository.findByUsername(username);
            assertEquals(fromDB, user);
        }
    }

}
