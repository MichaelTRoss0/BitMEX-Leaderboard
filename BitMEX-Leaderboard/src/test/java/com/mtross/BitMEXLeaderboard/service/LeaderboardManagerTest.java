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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class LeaderboardManagerTest {

    @Autowired
    private LeaderboardManager leaderboardManager;

    private List<User> TEST_USERS;
    private List<Leaderboard> TEST_LEADERBOARDS;
//    private List<String> TEST_SOURCES;

    public LeaderboardManagerTest() {
//        leaderboardService = new LeaderboardManagerImpl();

        TEST_USERS = Generator.generateTDUsers();
        TEST_LEADERBOARDS = Generator.generateTDLeaderboards(TEST_USERS);
//        TEST_SOURCES = Generator.generateTestSources(TEST_LEADERBOARDS);
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
    @Transactional
    public void testMergeLeaderboardIntoDatabase() {
        LocalDate day1 = LocalDate.EPOCH.plusDays(0);
        LocalDate day2 = LocalDate.EPOCH.plusDays(1);
        LocalDate day3 = LocalDate.EPOCH.plusDays(2);
        LocalDate day4 = LocalDate.EPOCH.plusDays(3);

        User user1 = new User();
        user1.setUsername("Name-One");
        user1.setRealName(true);
        Map<LocalDate, Integer> rankHistory1 = new HashMap<>();
        rankHistory1.put(day4, 3);
        user1.setRankHistory(rankHistory1);
        Map<LocalDate, String> profitHistory1 = new HashMap<>();
        profitHistory1.put(day4, "123123123123");
        user1.setProfitHistory(profitHistory1);

        User user2 = new User();
        user2.setUsername("Name-Two");
        user2.setRealName(false);
        Map<LocalDate, Integer> rankHistory2 = new HashMap<>();
        rankHistory2.put(day4, 2);
        user2.setRankHistory(rankHistory2);
        Map<LocalDate, String> profitHistory2 = new HashMap<>();
        profitHistory2.put(day4, "456456456456");
        user2.setProfitHistory(profitHistory2);

        User user3 = new User();
        user3.setUsername("Name-Three");
        user3.setRealName(true);
        Map<LocalDate, Integer> rankHistory3 = new HashMap<>();
        rankHistory3.put(day4, 1);
        user3.setRankHistory(rankHistory3);
        Map<LocalDate, String> profitHistory3 = new HashMap<>();
        profitHistory3.put(day4, "789789789789");
        user3.setProfitHistory(profitHistory3);

        Set<User> mergingUsers = new HashSet<>();
        mergingUsers.add(user1);
        mergingUsers.add(user2);
        mergingUsers.add(user3);

        Leaderboard mergingLeaderboard = new Leaderboard();
        mergingLeaderboard.setDate(day4);
        mergingLeaderboard.setUsers(mergingUsers);

        leaderboardManager.mergeLeaderboardIntoDatabase(mergingLeaderboard);

        List<Leaderboard> dbLeaderboards = leaderboardManager.findAllLeaderboards();
        List<User> dbUsers = leaderboardManager.findAllUsers();

        assertEquals(TEST_LEADERBOARDS.size() + 1, dbLeaderboards.size());
        assertEquals(TEST_USERS.size(), dbUsers.size());

        for (User user : dbUsers) {
            Map<LocalDate, Integer> rankHistory = user.getRankHistory();
            Map<LocalDate, String> profitHistory = user.getProfitHistory();

            assertTrue(rankHistory.containsKey(day1));
            assertTrue(rankHistory.containsKey(day2));
            assertTrue(rankHistory.containsKey(day3));
            assertTrue(rankHistory.containsKey(day4));

            assertTrue(profitHistory.containsKey(day1));
            assertTrue(profitHistory.containsKey(day2));
            assertTrue(profitHistory.containsKey(day3));
            assertTrue(profitHistory.containsKey(day4));
        }

//        for (Leaderboard leaderboard : dbLeaderboards) {
//            
//        }
    }

    /**
     * Test of createDifferenceTable method, of class LeaderboardManagerImpl.
     */
//    @Test
//    public void testCreateDifferenceTable() {
//    }

}
