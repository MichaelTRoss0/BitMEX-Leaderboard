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
import java.time.LocalDate;
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
public class LeaderboardRepositoryTest {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    private List<User> TEST_USERS;
    private List<Leaderboard> TEST_LEADERBOARDS;

    public LeaderboardRepositoryTest() {
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
     * Test of findByDate method, of class LeaderboardRepository.
     */
    @Test
    @Transactional
    public void testFindByDate() {
        for (Leaderboard leaderboard : TEST_LEADERBOARDS) {
            LocalDate date = leaderboard.getDate();
            Leaderboard fromDB = leaderboardRepository.findByDate(date);
            assertEquals(fromDB, leaderboard);
        }
    }

    /**
     * Test of findByDateBetween method, of class LeaderboardRepository.
     */
    @Test
    @Transactional
    public void testFindByDateBetween() {
        LocalDate date1 = LocalDate.EPOCH;
        LocalDate date2 = LocalDate.EPOCH.plusDays(1);
        LocalDate date3 = LocalDate.EPOCH.plusDays(2);

        List<Leaderboard> leaderboards12
                = leaderboardRepository.findByDateBetween(date1, date2);
        List<Leaderboard> leaderboards13
                = leaderboardRepository.findByDateBetween(date1, date3);
        List<Leaderboard> leaderboards23
                = leaderboardRepository.findByDateBetween(date2, date3);

        assertEquals(2, leaderboards12.size());
        assertTrue(leaderboards12.contains(leaderboardRepository.findByDate(date1)));
        assertTrue(leaderboards12.contains(leaderboardRepository.findByDate(date2)));

        assertEquals(3, leaderboards13.size());
        assertTrue(leaderboards13.contains(leaderboardRepository.findByDate(date1)));
        assertTrue(leaderboards13.contains(leaderboardRepository.findByDate(date2)));
        assertTrue(leaderboards13.contains(leaderboardRepository.findByDate(date3)));

        assertEquals(2, leaderboards23.size());
        assertTrue(leaderboards23.contains(leaderboardRepository.findByDate(date2)));
        assertTrue(leaderboards23.contains(leaderboardRepository.findByDate(date3)));
    }

}
