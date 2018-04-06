package com.nerisa.datarepo;

import com.nerisa.datarepo.job.UserEngagementJob;
import org.quartz.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by nerisa on 4/6/18.
 */
public class MyServletContextListener implements ServletContextListener {

    private Scheduler sched = null;
    private static Logger LOG = Logger.getLogger(MyServletContextListener.class.getSimpleName());


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOG.log(Level.INFO, "Starting jobs");
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        try {
            sched = schedFact.getScheduler();


            JobDetail job = newJob(UserEngagementJob.class)
                    .withIdentity("UserEngagementJob", "group1") // name "myJob", group "group1"
                    .build();

            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("engagementTrigger", "group1")
                    .startNow()
                    .withSchedule(cronSchedule("0 0/5 * * * ?"))
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.start();
            sched.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.log(Level.INFO, "Ending jobs");
        try {
            if (sched != null) {
                sched.shutdown(true);
            }
        }catch (SchedulerException e){
            e.printStackTrace();
        }
    }
}
