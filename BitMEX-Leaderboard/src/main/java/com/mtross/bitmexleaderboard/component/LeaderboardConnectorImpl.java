/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Component;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import com.mtross.bitmexleaderboard.entity.User;

/**
 *
 * @author mike
 */
@Component
public class LeaderboardConnectorImpl implements LeaderboardConnector {

    final String LEADERBOARD_URL = "https://www.bitmex.com/api/v1/leaderboard?method=notional";

    public LeaderboardConnectorImpl() {
    }

    @Override
    public Leaderboard makeLeaderboard()
            throws MalformedURLException, ProtocolException, IOException {

        HttpsURLConnection con = connectToLeaderboard();
        String response = getLeaderboardResponse(con);
        LocalDate date = LocalDate.now(Clock.systemUTC());
        Leaderboard leaderboard = buildLeaderboardEntity(response, date);

        return leaderboard;
    }

    private HttpsURLConnection connectToLeaderboard()
            throws MalformedURLException, ProtocolException, IOException {

        URL url = new URL(LEADERBOARD_URL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        return con;
    }

    private String getLeaderboardResponse(HttpsURLConnection con)
            throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return new String(content);
    }

    @Override
    public Leaderboard buildLeaderboardEntity(String response, LocalDate date) {
        Leaderboard leaderboard = new Leaderboard();

        leaderboard.setDate(date);
        leaderboard.setUsers(parseResponse(response, date));

        return leaderboard;
    }

    private Set<User> parseResponse(String response, LocalDate date) {
        Set<User> users = new HashSet<>();

        String modifiedResponse = response.replace("[{", "").replace("}]", "");
        modifiedResponse = modifiedResponse.replace("},{", "|");

        String[] userStrings = modifiedResponse.split("\\|");

        for (int i = 0; i < userStrings.length; i++) {
            String[] userTokens = userStrings[i].split(",");

            User currentUser = buildUserEntity(userTokens, date, i);

            users.add(currentUser);
        }

        return users;
    }

    private User buildUserEntity(String[] userTokens, LocalDate date, int i) {
        User currentUser = new User();

        String usernameToken = userTokens[2].replace("\"name\":", "").replace("\"", "");
        String realNameToken = userTokens[1].replace("\"isRealName\":", "");
        String profitToken = userTokens[0].replace("\"profit\":", "");

        Map<LocalDate, Integer> rankHistory = new HashMap<>();
        Map<LocalDate, String> profitHistory = new HashMap<>();

        rankHistory.put(date, i + 1);
        profitHistory.put(date, profitToken);

        currentUser.setUsername(usernameToken);
        currentUser.setRealName(Boolean.valueOf(realNameToken));
        currentUser.setRankHistory(rankHistory);
        currentUser.setProfitHistory(profitHistory);

		return currentUser;
	}

}
