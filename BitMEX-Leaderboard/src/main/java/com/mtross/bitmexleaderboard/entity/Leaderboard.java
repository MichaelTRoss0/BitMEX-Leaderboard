/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
@Entity
@Table(name = "Leaderboard")
public class Leaderboard {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private int leaderboardId;

    @Column(name = "`date`", unique = true, nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "Leaderboard_User",
            joinColumns = {
                @JoinColumn(name = "leaderboardId")},
            inverseJoinColumns = {
                @JoinColumn(name = "userId")})
    private Set<User> users;

}
