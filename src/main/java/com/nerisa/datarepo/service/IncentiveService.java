package com.nerisa.datarepo.service;

import com.nerisa.datarepo.incentive.CustodianLevel;
import com.nerisa.datarepo.incentive.CustodianTask;
import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.User;
import com.nerisa.datarepo.rdbms.DatabaseQuery;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 4/4/18.
 */
public class IncentiveService {

    private final static Logger LOG = Logger.getLogger(IncentiveService.class.getSimpleName());

    public static void addIncentive(User user, CustodianTask task){
        try {

            int pointsGained = task.getScore();
            int previousScore = DatabaseQuery.getUserScore(user);
            DatabaseQuery.updateUserScore(user, pointsGained);
            CustodianLevel previousLevel = getLevel(previousScore);
            CustodianLevel nextLevel = getLevel(pointsGained + previousScore);
            if((previousLevel != null && previousLevel != nextLevel) || (previousLevel == null && nextLevel != null)){
                //todo send upgrade notification
            }
        }catch (SQLException e ){
            LOG.log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }



    public static CustodianLevel getLevel(int score){
        CustodianLevel level = null;
        if(score < 200){

        } else if (score >= 200 && score < 500){
            level = CustodianLevel.NOVICE;
        } else if (score >= 500 && score < 1000){
            level = CustodianLevel.SEASONED;
        } else if (score >= 1000){
            level = CustodianLevel.EXPERT;
        }
        return level;
    }
}

