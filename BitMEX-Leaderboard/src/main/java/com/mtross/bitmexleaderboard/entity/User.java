/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.entity;

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
import javax.persistence.MapKeyTemporal;
import javax.persistence.Table;
import static javax.persistence.TemporalType.DATE;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
@Entity
@Table(name = "`User`")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private int userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private boolean realName;

    @ElementCollection
    @CollectionTable(name = "UserHistory",
            joinColumns = {
                @JoinColumn(name = "userId")})
    @MapKeyColumn(name = "`date`")
    @MapKeyTemporal(value = DATE)
    @Column(name = "`rank`")
    private Map<LocalDate, Integer> rankHistory = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "UserHistory",
            joinColumns = {
                @JoinColumn(name = "userId")})
    @MapKeyColumn(name = "`date`")
    @MapKeyTemporal(value = DATE)
    @Column(name = "profit")
    private Map<LocalDate, String> profitHistory = new HashMap<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Leaderboard> leaderboards;

}
