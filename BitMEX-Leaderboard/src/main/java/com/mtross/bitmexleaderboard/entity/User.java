/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.transaction.Transactional;

import lombok.Data;

/**
 *
 * @author mike
 */
@Data
@Entity
@Table(name = "`User`")
@SecondaryTable(name = "User_History",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
@Transactional
public class User implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "real_name", nullable = false)
    private boolean realName;

    @ElementCollection
    @CollectionTable(name = "User_History",
            joinColumns = {
                @JoinColumn(name = "user_id")})
    @MapKeyColumn(name = "`date`")
    @Column(name = "`rank`")
    private Map<LocalDate, Integer> rankHistory = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "User_History",
            joinColumns = {
                @JoinColumn(name = "user_id")})
    @MapKeyColumn(name = "`date`")
    @Column(name = "profit")
    private Map<LocalDate, String> profitHistory = new HashMap<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Leaderboard> leaderboards;

    public boolean appearsOnDay(LocalDate date) {

        Set<LocalDate> dateSet = this.rankHistory.keySet();
        return dateSet.contains(date);

    }

    public Integer getRankFromDate(LocalDate date) {

        return this.rankHistory.get(date);

    }

    public String getProfitFromDate(LocalDate date) {

        return this.profitHistory.get(date);

    }

}
