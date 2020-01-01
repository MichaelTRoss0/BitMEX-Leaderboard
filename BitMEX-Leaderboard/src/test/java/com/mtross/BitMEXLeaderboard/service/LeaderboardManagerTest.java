/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.service;

import com.mtross.BitMEXLeaderboard.Generator;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import com.mtross.bitmexleaderboard.service.LeaderboardManager;
import java.util.List;
//import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author mike
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LeaderboardManagerTest {

    @Autowired
    private LeaderboardManager leaderboardManager;

    private List<User> TEST_USERS;
    private List<Leaderboard> TEST_LEADERBOARDS;
    private List<String> TEST_SOURCES;

    public LeaderboardManagerTest() {
//        leaderboardService = new LeaderboardManagerImpl();

        TEST_USERS = Generator.generateTDUsers();
        TEST_LEADERBOARDS = Generator.generateTDLeaderboards(TEST_USERS);
        TEST_SOURCES = Generator.generateTestSources(TEST_LEADERBOARDS);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        leaderboardManager.deleteAllLeaderboards();
        leaderboardManager.deleteAllUsers();

        leaderboardManager.saveAllUsers(TEST_USERS);
        TEST_USERS = leaderboardManager.findAllUsers();

        leaderboardManager.saveAllLeaderboards(TEST_LEADERBOARDS);
        TEST_LEADERBOARDS = leaderboardManager.findAllLeaderboards();
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of mergeLeaderboardIntoDatabase method, of class
     * LeaderboardManagerImpl.
     */
    @Test
    public void testMergeLeaderboardIntoDatabase() {
    }

    /**
     * Test of createDifferenceTable method, of class LeaderboardManagerImpl.
     */
    @Test
    public void testCreateDifferenceTable() {
    }

}
