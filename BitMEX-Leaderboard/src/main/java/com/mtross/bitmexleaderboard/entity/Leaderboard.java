/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.entity;

import java.io.Serializable;
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
public class Leaderboard implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2458953251963141749L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "leaderboard_id")
    private int leaderboardId;

    @Column(name = "`date`", unique = true, nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "Leaderboard_User",
            joinColumns = {
                @JoinColumn(name = "leaderboard_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "user_id")})
    private Set<User> users;

    public User getUserByRank(Integer rank) {

        for (User user : this.users) {
            int userRank = user.getRankHistory().get(this.date);
            if (userRank == rank) {
                return user;
            }
        }

        return null;

    }

    public User getUserByUserame(String username) {

        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;

    }

}
