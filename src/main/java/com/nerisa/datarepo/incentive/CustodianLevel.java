package com.nerisa.datarepo.incentive;

/**
 * Created by nerisa on 4/4/18.
 */
public enum CustodianLevel {

    NOVICE(200), SEASONED(500), EXPERT(1000);

    private final int score;

    CustodianLevel(int score){
        this.score = score;
    }




}
