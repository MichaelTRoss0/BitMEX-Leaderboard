/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.dao;

import com.mtross.bitmexleaderboard.entity.Leaderboard;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mike
 */
@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Integer>,
        JpaSpecificationExecutor<Leaderboard> {

    public Leaderboard findByDate(LocalDate date);

    public List<Leaderboard> findByDateBetween(LocalDate startDate, LocalDate stopDate);

}
