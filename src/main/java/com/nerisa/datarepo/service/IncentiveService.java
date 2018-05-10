package com.nerisa.datarepo.service;

import com.nerisa.datarepo.incentive.CustodianLevel;
import com.nerisa.datarepo.incentive.CustodianTask;
import com.nerisa.datarepo.model.*;
import com.nerisa.datarepo.rdbms.DatabaseQuery;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 4/4/18.
 */
public class IncentiveService {

    private final static Logger LOG = Logger.getLogger(IncentiveService.class.getSimpleName());


    public void addIncentive(User user, CustodianTask task){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Adding incentive for user: " + user.getId());
        try {

            int pointsGained = task.getScore();
            int previousScore = databaseQuery.getUserScore(user);
            databaseQuery.updateUserScore(user, pointsGained);
            CustodianLevel previousLevel = getLevel(previousScore);
            CustodianLevel nextLevel = getLevel(pointsGained + previousScore);
            if((previousLevel != null && previousLevel != nextLevel) || (previousLevel == null && nextLevel != null)){
                //todo send upgrade notification
                LOG.log(Level.INFO, "User " + user.getId() + " has upgraded to " + nextLevel.toString());
                NotificationService.sendLevelUpgradeNotification(user, nextLevel);
            }
            databaseQuery.destroy();
        }catch (SQLException e ){
            LOG.log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    public void addIncentive(User custodian, Monument monument){
        if(monument.getReference()!= null){
            addIncentive(custodian, CustodianTask.ADD_WIKI);
        } else {
            addIncentive(custodian, CustodianTask.ADD_NEW);
        }
    }

    public void addIncentive(User custodian, Post post){
        if(custodian.getId() != post.getUserId()){
            addIncentive(custodian, CustodianTask.ADD_INFO);
        }
    }

    public void addIncentive(User custodian, Warning warning){
        if (warning.getId() == 1 && warning.isVerified()){
            addIncentive(custodian, CustodianTask.VERIFY_FIRST);
        } else if (warning.isVerified()){
            addIncentive(custodian, CustodianTask.VERIFY_WARNING);
        } else {
            addIncentive(custodian, CustodianTask.ADD_INFO);
        }
    }

    public void addIncentive(User custodian, NoiseData noiseData){
        addIncentive(custodian, CustodianTask.ADD_ENV_DATA);
    }


    public CustodianLevel getLevel(int score){
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

