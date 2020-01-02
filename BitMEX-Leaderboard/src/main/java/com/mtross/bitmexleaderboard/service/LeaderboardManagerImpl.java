/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.service;

import com.mtross.bitmexleaderboard.component.LeaderboardConnectorImpl;
import com.mtross.bitmexleaderboard.dao.LeaderboardRepository;
import com.mtross.bitmexleaderboard.dao.UserRepository;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ProtocolException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mike
 */
@Service
public class LeaderboardManagerImpl implements LeaderboardManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private LeaderboardConnectorImpl leaderboardConnector;

    final private DecimalFormat FORMATTER = new DecimalFormat("#,###.0000;(-#,###.0000)");
    final private String BTC = "\u20BF";

    public LeaderboardManagerImpl() {
    }

    @Transactional
    @Override
    public Leaderboard findLeaderboardByDate(LocalDate date) {
        return leaderboardRepository.findByDate(date);
    }

    @Transactional
    @Override
    public List<Leaderboard> findLeaderboardByDateBetween(LocalDate startDate, LocalDate stopDate) {
        return leaderboardRepository.findByDateBetween(startDate, stopDate);
    }

    @Transactional
    @Override
    public List<Leaderboard> findAllLeaderboards() {
        return leaderboardRepository.findAll();
    }

    @Transactional
    @Override
    public Leaderboard addLeaderboard(Leaderboard leaderboard) {
        return leaderboardRepository.saveAndFlush(leaderboard);
    }

    @Transactional
    @Override
    public List<Leaderboard> addAllLeaderboards(List<Leaderboard> leaderboards) {
        List<Leaderboard> savedLeaderboards = leaderboardRepository.saveAll(leaderboards);
        leaderboardRepository.flush();
        return savedLeaderboards;
    }

    @Transactional
    @Override
    public void deleteLeaderboardById(Integer id) {
        leaderboardRepository.deleteById(id);
        leaderboardRepository.flush();
    }

    @Transactional
    @Override
    public void deleteAllLeaderboards() {
        leaderboardRepository.deleteAll();
        leaderboardRepository.flush();
    }

    @Transactional
    @Override
    public void flushLeaderboards() {
        leaderboardRepository.flush();
    }

    @Transactional
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public List<User> addAllUsers(List<User> users) {
        List<User> savedUsers = userRepository.saveAll(users);
        userRepository.flush();
        return savedUsers;
    }

    @Transactional
    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

    @Transactional
    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Transactional
    @Override
    public void flushUsers() {
        userRepository.flush();
    }

    @Override
    public Leaderboard makeLeaderboard() throws ProtocolException, IOException {
        return leaderboardConnector.makeLeaderboard();
    }

    @Override
    @Transactional
    public void mergeLeaderboardIntoDatabase(Leaderboard leaderboard) {
        Set<User> leaderboardUsers = leaderboard.getUsers();

        Set<User> mergedUsers = createMergedUserSet(leaderboardUsers);
        //mergedUsers = new HashSet(addAllUsers(new ArrayList(mergedUsers)));

        Leaderboard leaderboardToBeMerged = new Leaderboard();
        leaderboardToBeMerged.setDate(leaderboard.getDate());
        leaderboardToBeMerged.setUsers(mergedUsers);

        addLeaderboard(leaderboard);
    }

    private Set<User> createMergedUserSet(Set<User> leaderboardUsers) {
        Set<User> mergedUsers = new HashSet<>();

        for (User user : leaderboardUsers) {
            String username = user.getUsername();
            User fromDB = findUserByUsername(username);

            if (fromDB != null) {
                fromDB = updateUser(fromDB, user);
                mergedUsers.add(fromDB);
//                User updatedUser = new User();
//
//                Map<LocalDate, Integer> rankHistory = user.getRankHistory();
//                rankHistory.putAll(fromDB.getRankHistory());
//
//                Map<LocalDate, String> profitHistory = user.getProfitHistory();
//                profitHistory.putAll(fromDB.getProfitHistory());
//
//                updatedUser.setUsername(username);
//                updatedUser.setRealName(user.isRealName());
//                updatedUser.setRankHistory(rankHistory);
//                updatedUser.setProfitHistory(profitHistory);
//
//                usersToBeMerged.add(updatedUser);
            } else {
                mergedUsers.add(user);
            }
        }
        flushUsers();

        return mergedUsers;
    }

    private User updateUser(User fromDB, User user) {
        User updatedUser = new User();
        
        String username = "";
        // realName
        // rankHistory
        // profitHistory
        
        updatedUser.setUsername(username);
        
        
        return updatedUser;
    }

    @Override
    public List<List<String>> createDifferenceTable(Leaderboard lbOld, Leaderboard lbNew) {
        List<List<String>> diffTable = new ArrayList<>();

        LocalDate oldDate = lbOld.getDate();
        LocalDate newDate = lbNew.getDate();

        List<String> tableHeader = new ArrayList<>();
        tableHeader.add("Rank (Change)");
        tableHeader.add("Name");
        tableHeader.add("Profit BTC");
        tableHeader.add("Change in Profit");
        diffTable.add(tableHeader);

        Set<User> users = lbNew.getUsers();
        for (User user : users) {
            List<String> userInfo = new ArrayList<>();

            String rankAndChange;
            String name;
            String profitBTC;
            String changeInProfit;

            Integer oldRank = user.getRankFromDate(oldDate);
            Integer newRank = user.getRankFromDate(newDate);
            rankAndChange = calculateChangeInRank(oldRank, newRank);

            name = user.getUsername();

            String oldProfit = user.getProfitFromDate(oldDate);
            String newProfit = user.getProfitFromDate(newDate);
            profitBTC = formatProfit(newProfit);
            changeInProfit = calculateChangeInProfit(oldProfit, newProfit);

            userInfo.add(rankAndChange);
            userInfo.add(name);
            userInfo.add(profitBTC);
            userInfo.add(changeInProfit);

            diffTable.add(userInfo);
        }

        return diffTable;
    }

    private String calculateChangeInRank(Integer oldRank, Integer newRank) {
        StringBuilder rankAndChange = new StringBuilder("");

        rankAndChange.append(newRank);

        if (oldRank != null) {
            int changeInRank = newRank - oldRank;

            if (changeInRank == 0) {
                rankAndChange.append(" (±0)");
            } else if (changeInRank > 0) {
                rankAndChange.append(" (+").append(changeInRank).append(")");
            } else if (changeInRank < 0) {
                rankAndChange.append(" (").append(changeInRank).append(")");
            }
        } else {
            rankAndChange.append(" (NEW)");
        }

        return new String(rankAndChange);
    }

    private String formatProfit(String profit) {
        String profitBTC;

        BigDecimal bdProfit = new BigDecimal(profit);
        bdProfit = bdProfit.divide(new BigDecimal("100000000")); // That's 10^8
        String formattedProfit = FORMATTER.format(bdProfit);

        profitBTC = BTC + " " + formattedProfit;

        return profitBTC;
    }

    private String calculateChangeInProfit(String oldProfit, String newProfit) {
        String changeInProfit;

        BigDecimal oldBdProfit = new BigDecimal(oldProfit);
        BigDecimal newBdProfit = new BigDecimal(newProfit);

        BigDecimal changeBdProfit = newBdProfit.subtract(oldBdProfit);
        changeBdProfit = changeBdProfit.divide(new BigDecimal("100000000")); // That's 10^8
        String formattedProfit = FORMATTER.format(changeBdProfit);

        changeInProfit = BTC + " " + formattedProfit;

        return changeInProfit;
    }

}
