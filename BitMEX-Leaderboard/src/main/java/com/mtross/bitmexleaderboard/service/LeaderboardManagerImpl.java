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
import java.math.RoundingMode;
import static java.math.RoundingMode.DOWN;
import java.net.ProtocolException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    final private int SCALE;
    final private RoundingMode ROUNDING_MODE;
    final private DecimalFormat FORMATTER;
    final private String BTC;

    public LeaderboardManagerImpl() {
        this.SCALE = 4;
        this.ROUNDING_MODE = DOWN;
        this.FORMATTER = new DecimalFormat("#,##0.0000;-#,##0.0000");
        FORMATTER.setRoundingMode(ROUNDING_MODE);
        this.BTC = "\u20BF";
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
        List<Leaderboard> fromDB = leaderboardRepository.findAll();
        List<Leaderboard> filteredList = new ArrayList<>(new HashSet<>(fromDB));
        return filteredList;
    }

    @Transactional
    @Override
    public Leaderboard saveLeaderboard(Leaderboard leaderboard) {
        return leaderboardRepository.saveAndFlush(leaderboard);
    }

    @Transactional
    @Override
    public List<Leaderboard> saveAllLeaderboards(List<Leaderboard> leaderboards) {
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
        List<User> fromDB = userRepository.findAll();
        List<User> filteredList = new ArrayList<>(new HashSet<>(fromDB));
        return filteredList;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public List<User> saveAllUsers(List<User> users) {
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
        mergedUsers = new HashSet(saveAllUsers(new ArrayList(mergedUsers)));

        Leaderboard leaderboardToBeMerged = new Leaderboard();
        leaderboardToBeMerged.setDate(leaderboard.getDate());
        leaderboardToBeMerged.setUsers(mergedUsers);

        saveLeaderboard(leaderboardToBeMerged);
    }

    private Set<User> createMergedUserSet(Set<User> leaderboardUsers) {
        Set<User> mergedUsers = new HashSet<>();

        for (User user : leaderboardUsers) {
            String username = user.getUsername();
            User fromDB = findUserByUsername(username);

            if (fromDB != null) {
                fromDB = updateUser(fromDB, user);
                mergedUsers.add(fromDB);
            } else {
                mergedUsers.add(user);
            }
        }
        flushUsers();

        return mergedUsers;
    }

    private User updateUser(User fromDB, User user) {
        User updatedUser = new User();

        int userId;
        String username;
        boolean realName;
        Map<LocalDate, Integer> rankHistory = new HashMap<>();
        Map<LocalDate, String> profitHistory = new HashMap<>();

        userId = fromDB.getUserId();
        username = fromDB.getUsername();
        realName = fromDB.isRealName();
        rankHistory.putAll(fromDB.getRankHistory());
        rankHistory.putAll(user.getRankHistory());
        profitHistory.putAll(fromDB.getProfitHistory());
        profitHistory.putAll(user.getProfitHistory());

        updatedUser.setUserId(userId);
        updatedUser.setUsername(username);
        updatedUser.setRealName(realName);
        updatedUser.setRankHistory(rankHistory);
        updatedUser.setProfitHistory(profitHistory);

        return updatedUser;
    }

    @Override
    public List<List<String>> buildDifferenceTable(Leaderboard lbOld, Leaderboard lbNew) {
        List<List<String>> diffTable = new ArrayList<>();

        LocalDate oldDate = lbOld.getDate();
        LocalDate newDate = lbNew.getDate();

        List<String> tableHeader = new ArrayList<>();
        tableHeader.add("Rank (Change)");
        tableHeader.add("Name");
        tableHeader.add("Profit BTC");
        tableHeader.add("Change in Profit");
        diffTable.add(tableHeader);

        //Set<User> users = lbNew.getUsers();
        //for (User user : users) {
        for (int i = 1; i <= lbNew.getUsers().size(); i++) {
            List<String> userInfo = new ArrayList<>();

            User currentUser = lbNew.getUserByRank(i);

            String rankAndChange;
            String name;
            String profitBTC;
            String changeInProfit;

            Integer oldRank = currentUser.getRankFromDate(oldDate);
            Integer newRank = currentUser.getRankFromDate(newDate);
            rankAndChange = calculateChangeInRank(oldRank, newRank);

            name = currentUser.getUsername();

            String oldProfit = currentUser.getProfitFromDate(oldDate);
            String newProfit = currentUser.getProfitFromDate(newDate);
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
            int changeInRank = oldRank - newRank;

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

        BigDecimal bdProfit = new BigDecimal(profit).setScale(SCALE, ROUNDING_MODE);
        bdProfit // 10^8
                = bdProfit.divide(new BigDecimal("100000000").setScale(SCALE, ROUNDING_MODE));
        String formattedProfit = FORMATTER.format(bdProfit);

        profitBTC = BTC + " " + formattedProfit;

        return profitBTC;
    }

    private String calculateChangeInProfit(String oldProfit, String newProfit) {
        String changeInProfit;

        BigDecimal oldBdProfit = new BigDecimal(oldProfit).setScale(SCALE, ROUNDING_MODE);
        BigDecimal newBdProfit = new BigDecimal(newProfit).setScale(SCALE, ROUNDING_MODE);

        BigDecimal changeBdProfit
                = newBdProfit.subtract(oldBdProfit).setScale(SCALE, ROUNDING_MODE);
        changeBdProfit // 10^8
                = changeBdProfit.divide(new BigDecimal("100000000").setScale(SCALE, ROUNDING_MODE));
        String formattedProfit = FORMATTER.format(changeBdProfit);

        changeInProfit = BTC + " " + formattedProfit;

        return changeInProfit;
    }

}
