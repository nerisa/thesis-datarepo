package com.nerisa.datarepo.incentive;

/**
 * Created by nerisa on 4/4/18.
 */
public enum CustodianTask {

    ADD_WIKI(200),
    ADD_NEW(300),
    ADD_INFO(50),
    ADD_ENV_DATA(100),
    ADD_PHOTO(100),
    VERIFY_FIRST(200),
    VERIFY_WARNING(100);


    private final int score;

    CustodianTask(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }
}
