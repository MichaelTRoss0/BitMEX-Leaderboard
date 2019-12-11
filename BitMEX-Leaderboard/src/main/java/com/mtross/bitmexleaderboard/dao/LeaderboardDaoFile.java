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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private Map<LocalDate, File> leaderboardFiles = new HashMap<>();
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
    public void makeUserMap(Map<String, User> mapOfUsers) {
        userMap = mapOfUsers;
    }

    @Override
    public Leaderboard addLeaderboard(Leaderboard leaderboard) {
        leaderboardHistory.put(leaderboard.getDate(), leaderboard);

        return leaderboard;
    }

    @Override
    public Leaderboard getLeaderboardByDate(LocalDate date) {
        if (leaderboardHistory.containsKey(date)) {
            return leaderboardHistory.get(date);
        } else {
            return null;
        }
    }

    @Override
    public List<Leaderboard> getAllLeaderboards() {
        return (List<Leaderboard>) leaderboardHistory.values();
    }

    @Override
    public void updateLeaderboard(LocalDate date, Leaderboard leaderboard) {
        leaderboard.setDate(date);

        File leaderboardFile = leaderboardFiles.get(date);

        leaderboardFiles.remove(date);
        leaderboardFiles.put(date, leaderboardFile);
        leaderboardHistory.replace(date, leaderboard);
    }

    @Override
    public void deleteLeaderboard(LocalDate date) {
        leaderboardFiles.remove(date);
        leaderboardHistory.remove(date);
    }

    @Override
    public Leaderboard makeTodaysLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        LocalDate date;
        List<User> users;

        date = findLeaderboardDate();
        List<HtmlElement> elements = scrapeDataFromBitmex();
        users = parseElements(date, elements);

        leaderboard.setDate(date);
        leaderboard.setUsers(users);

        return leaderboard;
    }

    private LocalDate findLeaderboardDate() {
        Clock utcClock = Clock.systemUTC();
        LocalDateTime currentTime = LocalDateTime.now(utcClock);
        if (currentTime.getHour() >= 12) {
            return LocalDate.now(utcClock);
        } else {
            return LocalDate.now(utcClock).minusDays(1);
        }
    }

    private List<HtmlElement> scrapeDataFromBitmex() {
        List<HtmlElement> elements;

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = null;
        try {
            page = client.getPage(URL_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        elements = page.getByXPath(
                "//ul[@class='obj collapsible']");

        return elements;
    }

    private List<User> parseElements(LocalDate date, List<HtmlElement> elements) {
        List<User> users = new ArrayList<>();

        int rank = 0;
        for (HtmlElement htmlElement : elements) {
            User user = new User();
            Map<LocalDate, Integer> rankHistory = new HashMap<>();
            Map<LocalDate, BigDecimal> profitHistory = new HashMap<>();
            BigDecimal profit;
            Boolean realName;
            String name;

            HtmlElement spanProfit = (HtmlElement) htmlElement.getFirstByXPath(
                    "//span[@class='type-number']");
            HtmlElement spanIsRealName = (HtmlElement) htmlElement.getFirstByXPath(
                    "//span[@class='type-boolean']");
            HtmlElement spanName = (HtmlElement) htmlElement.getFirstByXPath(
                    "//span[@class='type-string']");

            rank++;
            rankHistory.put(date, rank);
            profit = new BigDecimal(spanProfit.asText());
            profitHistory.put(date, profit);
            realName = Boolean.getBoolean(spanIsRealName.asText());
            name = spanName.asText();

            if (userMap.containsKey(name)) {
                User previousVersion = userMap.get(name);
                rankHistory.putAll(previousVersion.getRankHistory());
                profitHistory.putAll(previousVersion.getProfitHistory());
            }

            user.setRankHistory(rankHistory);
            user.setProfitHistory(profitHistory);
            user.setRealName(realName);
            user.setUserName(name);

            userMap.put(name, user);

            users.add(user);
        }

        return users;
    }

    private void mapLeaderboardFiles() {
        String[] splitLeaderboardFile = LEADERBOARD_FILE.split("/");

        File[] finder;
        File dir = new File(splitLeaderboardFile[0]);

        finder = dir.listFiles((File dir0, String filename)
                -> filename.endsWith(".txt"));

        for (File currentFile : finder) {
            String filename = currentFile.getName();
            LocalDate date = this.getDateFromFilename(filename);
            leaderboardFiles.put(date, currentFile);
        }
    }

    private void readLeaderboards() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void writeLeaderboards() throws PersistenceException {
        PrintWriter out = null;

        // Creates a dummy file so that the PrintWriter can be closed without issue
        String dummy = "dummy.txt";
        File dummyFile = new File(dummy);
        try {
            out = new PrintWriter(
                    new FileWriter(dummy));
        } catch (IOException ex) {
            Logger.getLogger(LeaderboardDaoFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (File currentFile : leaderboardFiles.values()) {
            String currentFilename = currentFile.getName();
            LocalDate currentDate = getDateFromFilename(currentFilename);
            Leaderboard datedLeaderboard = getLeaderboardByDate(currentDate);
            String leaderboardAsText;

            if (datedLeaderboard == null) {
                currentFile.delete();
            } else {

                try {
                    out = new PrintWriter(new FileWriter(currentFilename));
                } catch (IOException e) {
                    throw new PersistenceException(
                            "Could not save leaderboard data to file.", e);
                }

                // Print the legend at the top of each file
                out.println("Date|User1,User2,User3,User4,User5,User6,User7,"
                        + "User8,User9,User10,User11,User12,User13,User14,"
                        + "User15,User16,User17,User18,User19,User20,User21,"
                        + "User22,User23,User24,User25");
                out.flush();

                leaderboardAsText = marshallLeaderboard(datedLeaderboard);
                out.println(leaderboardAsText);
                out.flush();
            }
        }

        dummyFile.delete();
        out.close();
    }

    private Leaderboard unmarshallLeaderboard(String leaderboardAsText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String marshallLeaderboard(Leaderboard leaderboard) {
        String leaderboardAsText = "";
        leaderboardAsText += leaderboard.getDate() + "|";

        List<User> userList = leaderboard.getUsers();
        for (User user : userList) {
            String userAsText = marshallUser(user);
            if (userList.indexOf(user) != userList.size() - 1) {
                userAsText += ",";
            }
            leaderboardAsText += userAsText;
        }

        return leaderboardAsText;
    }

    private User unmarshallUser(String userAsText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String marshallUser(User user) {
//        String userAsText = "";
//        List<LocalDate> dates = 
//        for 
//        
//        return userAsText;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private LocalDate getDateFromFilename(String filename) {
        String[] splitFile = filename.split("_");
        String dateAsText = splitFile[1].replace(".txt", "");
        LocalDate date = LocalDate.parse(dateAsText,
                DateTimeFormatter.ofPattern(FORMAT));

        return date;
    }

    private String getFilenameFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        String dateString = date.format(formatter);
        String filename = LEADERBOARD_FILE.replace(FORMAT, dateString);

        return filename;
    }

}
