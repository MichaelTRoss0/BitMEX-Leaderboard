/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.bitmexleaderboard.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class Leaderboard {

    private LocalDate date;
    private List<User> users;

}
