/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mike
 */
public class Generator {

    public static List<User> generateTDUsers() {

        List<User> testUsers = new ArrayList<>();

        LocalDate day1 = LocalDate.EPOCH;
        LocalDate day2 = LocalDate.EPOCH.plusDays(1);
        LocalDate day3 = LocalDate.EPOCH.plusDays(2);

        User user1 = new User();
        user1.setUsername("Name-One");
        user1.setRealName(true);
        Map<LocalDate, Integer> rankHistory1 = new HashMap<>();
        rankHistory1.put(day1, 1);
        rankHistory1.put(day2, 2);
        rankHistory1.put(day3, 3);
        user1.setRankHistory(rankHistory1);
        Map<LocalDate, String> profitHistory1 = new HashMap<>();
        profitHistory1.put(day1, "333333333333");
        profitHistory1.put(day2, "555555555555");
        profitHistory1.put(day3, "777777777777");
        user1.setProfitHistory(profitHistory1);

        User user2 = new User();
        user2.setUsername("Name-Two");
        user2.setRealName(false);
        Map<LocalDate, Integer> rankHistory2 = new HashMap<>();
        rankHistory2.put(day1, 2);
        rankHistory2.put(day2, 3);
        rankHistory2.put(day3, 1);
        user2.setRankHistory(rankHistory2);
        Map<LocalDate, String> profitHistory2 = new HashMap<>();
        profitHistory2.put(day1, "222222222222");
        profitHistory2.put(day2, "444444444444");
        profitHistory2.put(day3, "999999999999");
        user2.setProfitHistory(profitHistory2);

        User user3 = new User();
        user3.setUsername("Name-Three");
        user3.setRealName(true);
        Map<LocalDate, Integer> rankHistory3 = new HashMap<>();
        rankHistory3.put(day1, 3);
        rankHistory3.put(day2, 1);
        rankHistory3.put(day3, 2);
        user3.setRankHistory(rankHistory3);
        Map<LocalDate, String> profitHistory3 = new HashMap<>();
        profitHistory3.put(day1, "111111111111");
        profitHistory3.put(day2, "666666666666");
        profitHistory3.put(day3, "888888888888");
        user3.setProfitHistory(profitHistory3);

        testUsers.add(user1);
        testUsers.add(user2);
        testUsers.add(user3);

        return testUsers;

    }

    public static List<Leaderboard> generateTDLeaderboards(List<User> users) {

        List<Leaderboard> testLeaderboards = new ArrayList<>();

        Set<LocalDate> dates = new HashSet<>();
        for (User user : users) {
            dates.addAll(user.getRankHistory().keySet());
        }

        for (LocalDate date : dates) {
            Leaderboard leaderboard = new Leaderboard();
            leaderboard.setDate(date);

            Set<User> leaderboardUsers = new HashSet<>();
            for (User user : users) {
                if (user.appearsOnDay(date)) {
                    leaderboardUsers.add(user);
                }
            }

            leaderboard.setUsers(leaderboardUsers);

            testLeaderboards.add(leaderboard);
        }

        return testLeaderboards;

    }

    public static List<String> generateTestSources(List<Leaderboard> leaderboards) {

        List<String> testSources = new ArrayList<>();

        for (Leaderboard leaderboard : leaderboards) {
            String testSource = generateTestSourceFromLeaderboard(leaderboard);
            testSources.add(testSource);
        }

        return testSources;

    }

    private static String generateTestSourceFromLeaderboard(Leaderboard leaderboard) {

        String testSource = "[";

        LocalDate date = leaderboard.getDate();
        for (int i = 1; i <= leaderboard.getUsers().size(); i++) {
            User currentUser = leaderboard.getUserByRank(i);

            String profitToken = currentUser.getProfitHistory().get(date);
            String realNameToken = Boolean.toString(currentUser.isRealName());
            String nameToken = currentUser.getUsername();

            String userString = "{";
            userString += "\"profit\":" + profitToken + ",";
            userString += "\"isRealName\":" + realNameToken + ",";
            userString += "\"name\":\"" + nameToken + "\"";
            userString += "}";

            if (i != leaderboard.getUsers().size()) {
                userString += ",";
            }

            testSource += userString;
        }

        testSource += "]";

        return testSource;

    }

}
