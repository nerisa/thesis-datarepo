package com.nerisa.datarepo.service;

import com.nerisa.datarepo.dao.*;
import com.nerisa.datarepo.firebase.Notification;
import com.nerisa.datarepo.incentive.CustodianTask;
import com.nerisa.datarepo.model.*;
import com.nerisa.datarepo.ontology.CidocSchema;
import com.nerisa.datarepo.ontology.GeoSchema;
import com.nerisa.datarepo.rdbms.DatabaseQuery;
import com.nerisa.datarepo.utils.Utility;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 2/15/18.
 */
public class MonumentService {

    public static final Logger LOG = Logger.getLogger(MonumentService.class.getName());


    /**
     *
     * @param fromLat
     * @param fromLong
     * @param within distance in meters
     * @return
     */
    public static List<Monument> getNearestMonuments(double fromLat, double fromLong, double within){
        LOG.log(Level.INFO, "Getting nearby monument from: (" + fromLat + "," + fromLong + ") within " + within );
        List<Monument> nearbyMonuments = new ArrayList<Monument>();
        final int EARTH_RADIUS = 6371000;
        double diffLat =  Math.toDegrees(within/EARTH_RADIUS);
        double maxLat = fromLat + diffLat;
        double minLat = fromLat - diffLat;
        double diffLong = Math.toDegrees(Math.asin(diffLat)) / Math.cos(Math.toRadians(fromLat));
        double maxLong = fromLong + diffLong;
        double minLong = fromLong - diffLong;

        try {
            nearbyMonuments = MonumentDao.getNearestMonuments(minLat, maxLat, minLong, maxLong);
        } catch (Exception e){
           e.printStackTrace();
        }
        return nearbyMonuments;
    }

    public static List<Monument> getAllMonuments(){
        Monument m1 = new Monument(1l,"Sud Paris", "me", "Hellooooo", "Hellooooo", 48.624061, 2.444167);
        Monument m2 = new Monument(2l, "rue Charles Fourier", "me", "Hellooooo", "Hellooooo",48.625082, 2.443458);
        Monument m3 = new Monument(3l,"Paul Eluard", "me", "Hellooooo", "Hellooooo",48.623109, 2.443289);
        Monument m4 = new Monument(4l,"Coquibus", "me", "Hellooooo", "Hellooooo",48.623458, 2.441631);
        Monument m5 = new Monument(5l,"Genescope", "me", "Hellooooo","Hellooooo", 48.623590, 2.439283);
        Monument m6 = new Monument(6l,"Buddhist Temple", "me", "Hellooooo","Hellooooo", 48.627267, 2.443837);
        Monument m7 = new Monument(7l,"Cemetary", "me", "Hellooooo","Hellooooo", 48.627608, 2.439138);
        List<Monument> monuments = new ArrayList<Monument>();
        monuments.add(m1);
        monuments.add(m2);
        monuments.add(m3);
        monuments.add(m4);
        monuments.add(m5);
        monuments.add(m6);
        monuments.add(m7);
        return monuments;
    }

    public static Monument getMonument(Long id){
        Monument monument = null;
        try{
            monument = MonumentDao.getMonument(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return monument;
    }



    public static Post addPost(Post post, Monument monument){
        try {
            Long postId = DatabaseQuery.getNextPostId(monument.getId());
            if(postId == 0){
                postId = postId + 1l;
            }
            post.setId(postId);
            PostDao.createPost(post, monument);
            DatabaseQuery.incrementPostId(monument.getId());
        } catch (Exception e){
            e.printStackTrace();
        }
        return post;
    }

    public static List<Post> getPosts(Long monumentId){
//        Post post1 = new Post("I was here", new Date().getTime());
//        Post post2 = new Post("I was here2", new Date().getTime());
//        Post post3 = new Post("I was here3", new Date().getTime());
//        List<Post> postList = new ArrayList<Post>();
//        postList.add(post1);
//        postList.add(post2);
//        postList.add(post3);
//        return postList;
        Monument monument = new Monument();
        monument.setId(monumentId);
        List<Post> posts = new ArrayList<Post>();
        try{
            posts = PostDao.getPosts(monument);
        }catch (Exception e){
            e.printStackTrace();
        }
        return posts;
    }


    public static TemperatureData getTemp(){
        TemperatureData  temp = new TemperatureData();
        temp.setDate(new Date().getTime());
        temp.setId(1l);
        temp.setValue(78.0);
        return temp;
    }

    public static Monument saveMonument(Monument monument){
        LOG.log(Level.INFO, "saving monument data for " + monument.getName());
        Long monumentId = 0l;
        Long warningCount = 0l;
        Long postCount = 0l;
        Long tempCount = 0l;
        Long noiseCount = 0l;
        User custodian = null;
        try{
            custodian = DatabaseQuery.getUser(monument.getCustodian().getId());
            LOG.log(Level.INFO, "Monument custodian: " + custodian.getId());
//            Set all information for custodian
            monument.setCustodian(custodian);
            monumentId = DatabaseQuery.getLastMonumentId() + 1;
            monument.setId(monumentId);
            MonumentDao.createMonument(monument);
            if(!monument.getTemperatures().isEmpty()) {
                for (TemperatureData temperatureData : monument.getTemperatures()) {
                    temperatureData.setId(tempCount + 1);
                    TemperatureDao.addTemperatureData(temperatureData, monument);
                    tempCount++;
                }
            }
            if(!monument.getNoiseProfiles().isEmpty()) {
                for (NoiseData noiseData : monument.getNoiseProfiles()) {
                    noiseData.setId(noiseCount + 1);
                    NoiseDao.addNoiseData(noiseData, monument);
                    noiseCount++;
                }
            }
            if(!monument.getWarnings().isEmpty()) {
                for (Warning warning : monument.getWarnings()) {
                    DatabaseQuery.saveWarning(warning, monumentId);
                }
            }
            if(!monument.getPosts().isEmpty()) {
                for (Post post : monument.getPosts()) {
                    post.setId(postCount + 1);
                    PostDao.createPost(post, monument);
                    postCount++;
                }
            }
            DatabaseQuery.addMonumentResourceIds(monumentId, warningCount,postCount,tempCount,noiseCount);
            DatabaseQuery.incrementMonumentId();
            DatabaseQuery.changeCustodianStatus(Boolean.TRUE, custodian);
            if(monument.getReference()!= null){
                IncentiveService.addIncentive(custodian, CustodianTask.ADD_WIKI);
            } else {
                IncentiveService.addIncentive(custodian, CustodianTask.ADD_NEW);
            }
            DatabaseQuery.updateMonumentId(custodian, monumentId);
            LOG.log(Level.INFO, "Monument saved for " + monument.getName());
            return monument;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }



    }


    public static Warning addUnverifiedWarning(Warning warning, Long monumentId){
        LOG.log(Level.INFO, "Warning posted for monument" + monumentId);
        Warning savedWarning = new Warning();
        Monument monument = MonumentDao.getMonument(monumentId);
        if(monument == null){
            return null;
        }
        try {
            if(monument.getCustodian().getId() == warning.getUserId()){
                savedWarning = addVerifiedWarning(warning, monument);
            } else {
                savedWarning = DatabaseQuery.saveWarning(warning, monumentId);
                User custodian = DatabaseQuery.getUser(monument.getCustodian().getId());
                Notification notification = new Notification("New warning", "A new warning has been posted for your monument", custodian.getToken());
                JSONObject warningObject = savedWarning.prepareNotificationBody(monumentId);
                notification.sendNotification(warningObject);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        //TODO get the token for user of the monument

        return savedWarning;
    }

    public static Warning addVerifiedWarning(Warning warning, Monument monument){
        LOG.log(Level.INFO, "Adding a verified warning for monument" + monument.getName());
        try {
            Long warningId = DatabaseQuery.getNextWarningId(monument.getId());
            if(warningId == 0){
                warningId = warningId + 1l;
            }
            warning.setId(warningId);
            WarningDao.createWarning(warning, monument, warningId);
            DatabaseQuery.incrementWarningId(monument.getId());
            warning.setVerified(Boolean.TRUE);
        } catch(Exception e){
            e.printStackTrace();
        }
        return warning;
    }


    public static List<Warning> getWarnings(Monument monument){
//        Warning warning1 = new Warning("This monument has cracks", "https://firebasestorage.googleapis.com/v0/b/custodian-3e7c1.appspot.com/o/images%2F1d32ef44-0eec-46ca-8001-e47d79646371?alt=media&token=c8466137-e2a3-4e56-b2a1-01554c3c5b0f", new Date().getTime());
//        Warning warning2 = new Warning("This monument has cracks", "https://firebasestorage.googleapis.com/v0/b/custodian-3e7c1.appspot.com/o/images%2F1d32ef44-0eec-46ca-8001-e47d79646371?alt=media&token=c8466137-e2a3-4e56-b2a1-01554c3c5b0f", new Date().getTime());
//        Warning warning3 = new Warning("This monument has cracks", "https://firebasestorage.googleapis.com/v0/b/custodian-3e7c1.appspot.com/o/images%2F1d32ef44-0eec-46ca-8001-e47d79646371?alt=media&token=c8466137-e2a3-4e56-b2a1-01554c3c5b0f", new Date().getTime());
//        List<Warning> warningList = new ArrayList<Warning>();
//        warningList.add(warning1);
//        warningList.add(warning2);
//        warningList.add(warning3);
//        return warningList;

        List<Warning> warnings = new ArrayList<Warning>();
        try{
            warnings = WarningDao.getVerifiedWarnings(monument);
        } catch (Exception e){
            e.printStackTrace();
        }
        return warnings;
    }

    public static void changeWarningStatus(Warning warning, Long monumentId){
        Monument monument = new Monument();
        monument.setId(monumentId);
        try {
            Long verifiedWarningId = DatabaseQuery.getNextWarningId(monumentId);
            WarningDao.createWarning(warning, monument, verifiedWarningId);
            DatabaseQuery.incrementWarningId(monumentId);
            DatabaseQuery.deleteWarning(warning);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Warning> getUnverifiedWarnings(Monument monument){
        List<Warning> unverifiedWarningList = new ArrayList<Warning>();
        try{
            unverifiedWarningList = DatabaseQuery.getWarnings(monument);
        }catch (SQLException e){
            LOG.log(Level.SEVERE, "Error when retrieving unverified warning for monument " + monument.getId() + ":\n" + e.getStackTrace());
        }
        return unverifiedWarningList;
    }

    public static User createUser(User user){
        User savedUser = null;
        try{
            User existingUser = DatabaseQuery.getUser(user.getEmail());
            if(existingUser.getId() != null) {
                LOG.log(Level.INFO, "User with email " + user.getEmail() + " already exists");
                existingUser.setToken(user.getToken());
                updateUserToken(existingUser);
                savedUser = existingUser;
            } else {
                LOG.log(Level.INFO, "Creating new user: " + user.getEmail());

                user.setLastLoggedIn(new Date().getTime());
                user.setCustodian(Boolean.FALSE);

                savedUser = DatabaseQuery.createUser(user);
            }
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
        LOG.log(Level.INFO, "User "+ user.getEmail() + " has id " + savedUser.getId());
        return savedUser;
    }

    public static boolean updateUserToken(User user){
        LOG.log(Level.INFO, "Updating user token for " + user.getToken());
        boolean success = Boolean.FALSE;
        try{
            success = DatabaseQuery.updateUserToken(user);
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return success;
    }

}
