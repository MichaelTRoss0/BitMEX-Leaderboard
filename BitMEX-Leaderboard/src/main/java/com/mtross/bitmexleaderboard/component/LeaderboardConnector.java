/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.time.LocalDate;

import com.mtross.bitmexleaderboard.entity.Leaderboard;

/**
 *
 * @author mike
 */
public interface LeaderboardConnector {

	public Leaderboard makeLeaderboard() throws MalformedURLException, ProtocolException, IOException;

	public Leaderboard buildLeaderboardEntity(String response, LocalDate date);

}
