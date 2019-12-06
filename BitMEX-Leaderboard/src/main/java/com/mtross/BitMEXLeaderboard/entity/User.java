/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtross.BitMEXLeaderboard.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author mike
 */
@Data
public class User {

    private Map<LocalDate, Integer> rank;
    private Map<LocalDate, BigDecimal> profit;
    private boolean realName;
    private String userName;

}
