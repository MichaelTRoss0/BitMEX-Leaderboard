/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mtross.bitmexleaderboard.daoexceptions.PersistenceException;
import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mike
 */
@Repository
public class LeaderboardDaoFile implements LeaderboardDao {

    public static String LEADERBOARD_FILE;
    public static final String FORMAT = "MMddyyyy";
    public static final String DELIMITER = ",";
    public static final String URL_STRING
            = "https://www.bitmex.com/api/v1/leaderboard?method=notional";

    public LeaderboardDaoFile() throws MalformedURLException {
        LEADERBOARD_FILE = "Data/Leaderboard_MMddyyyy.txt";
    }

    public LeaderboardDaoFile(String filename) throws MalformedURLException {
        LEADERBOARD_FILE = filename;
    }

    private Map<Leaderboard, File> leaderboardFiles = new HashMap<>();
    private Map<LocalDate, Leaderboard> leaderboardHistory = new HashMap<>();
    private Map<String, User> userMap = new HashMap<>();

    @Override
    public void loadFromFiles() throws PersistenceException {
        mapLeaderboardFiles();
        readLeaderboards();
    }

    @Override
    public void saveToAllFiles() throws PersistenceException {
        writeLeaderboards();
    }

    @Override
    public Leaderboard addLeaderboard() {
        Leaderboard leaderboard = makeTodaysLeaderboard();

        return leaderboard;
    }

    @Override
    public Leaderboard addLeaderboard(Leaderboard leaderboard) {
        leaderboardHistory.put(leaderboard.getDate(), leaderboard);

        return leaderboard;
    }

    @Override
    public Leaderboard getLeaderboardByDate(LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Leaderboard> getAllLeaderboards() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLeaderboard(Leaderboard leaderboard) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteLeaderboard(LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void mapLeaderboardFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void readLeaderboards() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void writeLeaderboards() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Leaderboard makeTodaysLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        LocalDate date;
        List<User> users;

        Clock utcClock = Clock.systemUTC();
        LocalDateTime currentTime = LocalDateTime.now(utcClock);
        if (currentTime.getHour() >= 12) {
            date = LocalDate.now(utcClock);
        } else {
            date = LocalDate.now(utcClock).minusDays(1);
        }
        leaderboard.setDate(date);

        users = scrapeDataFromBitmex();

        return leaderboard;
    }

    private List<User> scrapeDataFromBitmex() {
        List<User> users = new ArrayList<>();

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = null;
        try {
            page = client.getPage(URL_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<HtmlElement> objects = page.getByXPath(
                "//ul[@class='obj collapsible']");
        int rank = 0;
        for (HtmlElement htmlObject : objects) {
            User user = new User();
            rank++;
            BigDecimal profit;
            Boolean realName;
            String name;

            HtmlElement spanProfit = (HtmlElement) htmlObject.getFirstByXPath(
                    "//span[@class='type-number']");
            HtmlElement spanIsRealName = (HtmlElement) htmlObject.getFirstByXPath(
                    "//span[@class='type-boolean']");
            HtmlElement spanName = (HtmlElement) htmlObject.getFirstByXPath(
                    "//span[@class='type-string']");

            name = spanName.asText();
            user.setUserName(name);
        }

        return users;
    }

}
