package com.nerisa.datarepo.job;

import com.nerisa.datarepo.dao.NoiseDao;
import com.nerisa.datarepo.model.NoiseData;
import com.nerisa.datarepo.model.User;
import com.nerisa.datarepo.rdbms.DatabaseQuery;
import com.nerisa.datarepo.service.IncentiveService;
import com.nerisa.datarepo.service.MonumentService;
import com.nerisa.datarepo.service.NotificationService;
import com.nerisa.datarepo.utils.Constant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 4/6/18.
 */
public class UserEngagementJob implements Job {

    private static Logger LOG = Logger.getLogger(UserEngagementJob.class.getSimpleName());
    public UserEngagementJob(){}

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.log(Level.INFO, "Checking user engagements");
//        Long oldTime = (System.currentTimeMillis() - (Constant.OLD_DATA_DAYS * Constant.DAY_IN_MS));
        Long oldTime = System.currentTimeMillis();
        try {
            List<User> custodians = DatabaseQuery.getAllOldCustodians();
            for(User user: custodians){
                Long latestNoiseId = DatabaseQuery.getNextNoiseId(user.getMonumentId()) - 1;
                NoiseData noiseData = NoiseDao.getNoiseData(latestNoiseId, user.getMonumentId());
                if (noiseData.getDate() < oldTime){
                    LOG.log(Level.INFO, "User " + user.getId() + " is being sent a notification");
                    NotificationService.sendGetDataNotification(user);
                }
            }
        }catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }
}
