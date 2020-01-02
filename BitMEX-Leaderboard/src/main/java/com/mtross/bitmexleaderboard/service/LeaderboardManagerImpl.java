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
    public Leaderboard saveLeaderboard(Leaderboard leaderboard) {
        return leaderboardRepository.save(leaderboard);
    }

    @Transactional
    @Override
    public List<Leaderboard> saveAllLeaderboards(List<Leaderboard> leaderboards) {
        return leaderboardRepository.saveAll(leaderboards);
    }

    @Transactional
    @Override
    public void deleteLeaderboardById(Integer id) {
        leaderboardRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllLeaderboards() {
        leaderboardRepository.deleteAll();
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
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public List<User> saveAllUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Transactional
    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public Leaderboard makeLeaderboard() throws ProtocolException, IOException {
        return leaderboardConnector.makeLeaderboard();
    }

    @Override
    public void mergeLeaderboardIntoDatabase(Leaderboard leaderboard) {
        Set<User> leaderboardUsers = leaderboard.getUsers();

        Set<User> usersToBeMerged = createMergedUserSet(leaderboardUsers);

        Leaderboard leaderboardToBeMerged = new Leaderboard();
        leaderboardToBeMerged.setDate(leaderboard.getDate());
        leaderboardToBeMerged.setUsers(usersToBeMerged);

        this.saveAllUsers(new ArrayList(usersToBeMerged));
        this.saveLeaderboard(leaderboard);
    }

    private Set<User> createMergedUserSet(Set<User> leaderboardUsers) {
        Set<User> usersToBeMerged = new HashSet<>();

        for (User user : leaderboardUsers) {
            String username = user.getUsername();
            User fromDB = this.findUserByUsername(username);

            if (fromDB != null) {
                User updatedUser = new User();

                Map<LocalDate, Integer> rankHistory = user.getRankHistory();
                rankHistory.putAll(fromDB.getRankHistory());

                Map<LocalDate, String> profitHistory = user.getProfitHistory();
                profitHistory.putAll(fromDB.getProfitHistory());

                updatedUser.setUsername(username);
                updatedUser.setRealName(user.isRealName());
                updatedUser.setRankHistory(rankHistory);
                updatedUser.setProfitHistory(profitHistory);

                usersToBeMerged.add(updatedUser);
            } else {
                usersToBeMerged.add(user);
            }
        }

        return usersToBeMerged;
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
