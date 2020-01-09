/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mtross.BitMEXLeaderboard.Generator;
import com.mtross.bitmexleaderboard.component.LeaderboardConnector;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;

/**
 *
 * @author mike
 */
@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
//@DataJpaTest
//@ContextConfiguration
public class LeaderboardConnectorTest {

    @Autowired
    private LeaderboardConnector leaderboardConnector;

    private List<User> TEST_USERS;
    private List<Leaderboard> TEST_LEADERBOARDS;
    private List<String> TEST_SOURCES;

    public LeaderboardConnectorTest() {
//        leaderboardConnector = new LeaderboardConnectorImpl();

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
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of makeLeaderboard method, of class LeaderboardConnectorImpl.
     *
     */
    @Test
    public void testMakeLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();

        try {
            leaderboard = leaderboardConnector.makeLeaderboard();
        } catch (IOException e) {
            fail();
        }

        LocalDate date = leaderboard.getDate();
        Set<User> users = leaderboard.getUsers();

        assertNotNull(date);
        assertNotNull(users);
        assertNotEquals(users.size(), 0);

        for (User user : users) {
            String username = user.getUsername();
            boolean realName = user.isRealName();
            Map<LocalDate, Integer> rankHistory = user.getRankHistory();
            Map<LocalDate, String> profitHistory = user.getProfitHistory();

            assertNotNull(username);
            assertNotNull(realName);
            assertNotNull(rankHistory);
            assertNotEquals(rankHistory.size(), 0);
            assertNotNull(profitHistory);
            assertNotEquals(profitHistory.size(), 0);
        }
    }

    /**
     * Test of buildLeaderboardEntity method, of class LeaderboardConnectorImpl.
     */
    @Test
    public void testBuildLeaderboardEntity() {

        int i = 0;
        for (String source : TEST_SOURCES) {
            LocalDate date = LocalDate.EPOCH.plusDays(i);

            Leaderboard fromConnector
                    = leaderboardConnector.buildLeaderboardEntity(source, date);

            Leaderboard fromGlobalVar = new Leaderboard();
            for (Leaderboard leaderboardG : TEST_LEADERBOARDS) {
                if (leaderboardG.getDate().equals(date)) {
                    fromGlobalVar = leaderboardG;
                    break;
                }
            }

            Set<User> usersC = fromConnector.getUsers();
            for (User userC : usersC) {
                String usernameC = userC.getUsername();
                boolean realNameC = userC.isRealName();
                Map<LocalDate, Integer> rankHistoryC = userC.getRankHistory();
                Map<LocalDate, String> profitHistoryC = userC.getProfitHistory();

                User userG = fromGlobalVar.getUserByUserame(usernameC);
                String usernameG = userG.getUsername();
                boolean realNameG = userG.isRealName();
                Map<LocalDate, Integer> rankHistoryG = userG.getRankHistory();
                Map<LocalDate, String> profitHistoryG = userG.getProfitHistory();

                assertEquals(usernameC, usernameG);
                assertEquals(realNameC, realNameG);
                assertTrue(rankHistoryC.containsKey(date));
                assertTrue(rankHistoryG.containsValue(rankHistoryC.get(date)));
                assertTrue(profitHistoryC.containsKey(date));
                assertTrue(profitHistoryG.containsValue(profitHistoryC.get(date)));
            }

            i++;
        }

    }

}
