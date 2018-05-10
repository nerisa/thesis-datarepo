package com.nerisa.datarepo.service;

import com.nerisa.datarepo.dao.*;
import com.nerisa.datarepo.model.*;
import com.nerisa.datarepo.rdbms.DatabaseQuery;
import org.json.simple.JSONObject;

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
//    DatabaseQuery databaseQuery = new DatabaseQuery();
    IncentiveService incentiveService = new IncentiveService();

    /**
     *
     * @param fromLat
     * @param fromLong
     * @param within distance in meters
     * @return
     */
    public List<Monument> getNearestMonuments(double fromLat, double fromLong, double within){
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


    public Monument getMonument(Long id){
        Monument monument = null;
        try{
            monument = MonumentDao.getMonument(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return monument;
    }



    public Post addPost(Post post, Monument monument){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        try {
            Long postId = databaseQuery.getNextPostId(monument.getId());
            if(postId == 0){
                postId = postId + 1l;
            }
            post.setId(postId);
            PostDao.createPost(post, monument);
            incentiveService.addIncentive(monument.getCustodian(), post);
            databaseQuery.incrementPostId(monument.getId());
            databaseQuery.destroy();
        } catch (Exception e){
            e.printStackTrace();
        }
        return post;
    }

    public List<Post> getPosts(Long monumentId){
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


    public TemperatureData getTemp(){
        TemperatureData  temp = new TemperatureData();
        temp.setDate(new Date().getTime());
        temp.setId(1l);
        temp.setValue(78.0);
        return temp;
    }

    public Monument saveMonument(Monument monument){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "saving monument data for " + monument.getName());
        Long monumentId = 0l;
        Long warningCount = 0l;
        Long postCount = 0l;
        Long tempCount = 0l;
        Long noiseCount = 0l;
        User custodian = null;
        try{
            custodian = databaseQuery.getUser(monument.getCustodian().getId());
            LOG.log(Level.INFO, "Monument custodian: " + custodian.getId());
//            Set all information for custodian
            monument.setCustodian(custodian);
            monumentId = databaseQuery.getLastMonumentId() + 1;
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
                    databaseQuery.saveWarning(warning, monumentId);
                }
            }
            if(!monument.getPosts().isEmpty()) {
                for (Post post : monument.getPosts()) {
                    post.setId(postCount + 1);
                    PostDao.createPost(post, monument);
                    postCount++;
                }
            }
            databaseQuery.addMonumentResourceIds(monumentId, warningCount,postCount,tempCount,noiseCount);
            databaseQuery.incrementMonumentId();
            databaseQuery.changeCustodianStatus(Boolean.TRUE, custodian);
            incentiveService.addIncentive(custodian, monument);
            databaseQuery.updateMonumentId(custodian, monumentId);
            databaseQuery.destroy();
            LOG.log(Level.INFO, "Monument saved for " + monument.getName());
            return monument;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    public Warning addUnverifiedWarning(Warning warning, Long monumentId){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Warning posted for monument" + monumentId);
        Warning savedWarning = new Warning();
        Monument monument = MonumentDao.getMonument(monumentId);
        if(monument == null){
            return null;
        }
        User custodian = null;
        try {
            if(monument.getCustodian().getId() == warning.getUserId()){
                savedWarning = addVerifiedWarning(warning, monument);
            } else {
                savedWarning = databaseQuery.saveWarning(warning, monumentId);
                custodian = databaseQuery.getUser(monument.getCustodian().getId());
                NotificationService.sendNewWarningNotification(savedWarning, monumentId, custodian);
                incentiveService.addIncentive(custodian, warning);
            }
            databaseQuery.destroy();
        } catch(Exception e){
            e.printStackTrace();
        }

        return savedWarning;
    }

    public Warning addVerifiedWarning(Warning warning, Monument monument){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Adding a verified warning for monument" + monument.getName());
        try {
            Long warningId = databaseQuery.getNextWarningId(monument.getId());
            if(warningId == 0){
                warningId = warningId + 1l;
            }
            warning.setId(warningId);
            WarningDao.createWarning(warning, monument, warningId);
            databaseQuery.incrementWarningId(monument.getId());
            warning.setVerified(Boolean.TRUE);
            User custodian = databaseQuery.getUser(monument.getCustodian().getId());
            incentiveService.addIncentive(custodian,warning);
            databaseQuery.destroy();
        } catch(Exception e){
            e.printStackTrace();
        }
        return warning;
    }


    public List<Warning> getWarnings(Monument monument){
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

    public void changeWarningStatus(Warning warning, Long monumentId){
        DatabaseQuery databaseQuery = new DatabaseQuery();
       Monument monument = MonumentDao.getMonument(monumentId);
        try {
            if(warning.isVerified()) {
                Long verifiedWarningId = databaseQuery.getNextWarningId(monumentId);
                WarningDao.createWarning(warning, monument, verifiedWarningId);
                databaseQuery.incrementWarningId(monumentId);
                databaseQuery.deleteWarning(warning);
                warning.setId(verifiedWarningId);
            } else {
                databaseQuery.deleteWarning(warning);
            }
            User user = databaseQuery.getUser(monument.getCustodian().getId());
            databaseQuery.destroy();
            incentiveService.addIncentive(user,warning);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Warning> getUnverifiedWarnings(Monument monument){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        List<Warning> unverifiedWarningList = new ArrayList<Warning>();
        try{
            unverifiedWarningList = databaseQuery.getWarnings(monument);
            databaseQuery.destroy();
        }catch (SQLException e){
            LOG.log(Level.SEVERE, "Error when retrieving unverified warning for monument " + monument.getId() + ":\n" + e.getStackTrace());
        }
        return unverifiedWarningList;
    }

    public User createUser(User user){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        User savedUser = null;
        try{
            User existingUser = databaseQuery.getUser(user.getEmail());
            if(existingUser.getId() != null) {
                LOG.log(Level.INFO, "User with email " + user.getEmail() + " already exists");
                existingUser.setToken(user.getToken());
                updateUserToken(existingUser);
                savedUser = existingUser;
            } else {
                LOG.log(Level.INFO, "Creating new user: " + user.getEmail());

                user.setLastLoggedIn(new Date().getTime());
                user.setCustodian(Boolean.FALSE);
                savedUser = databaseQuery.createUser(user);
            }
            databaseQuery.destroy();
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
        if(savedUser!= null) {
            LOG.log(Level.INFO, "User " + user.getEmail() + " has id " + savedUser.getId());
        }
        return savedUser;
    }


    public boolean updateUserToken(User user){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Updating user token for " + user.getToken());
        boolean success = Boolean.FALSE;
        try{
            success = databaseQuery.updateUserToken(user);
            databaseQuery.destroy();
        } catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return success;
    }

    public void addNoise(Monument monument, NoiseData noise){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Adding noise for monument " + monument.getId());
        try {
            Long noiseId = databaseQuery.getNextNoiseId(monument.getId());
            noise.setId(noiseId);
            NoiseDao.addNoiseData(noise, monument);
            databaseQuery.incrementNoiseId(monument.getId());
            User user = databaseQuery.getUser(monument.getCustodian().getId());
            databaseQuery.destroy();
            incentiveService.addIncentive(user,noise);
        }catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    public  void addTemperature(Monument monument, TemperatureData temperatureData){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        LOG.log(Level.INFO, "Adding temperature data for monument " + monument.getId());
        try{
            Long temperatureId = databaseQuery.getNextTemperatureId(monument.getId());
            temperatureData.setId(temperatureId);
            TemperatureDao.addTemperatureData(temperatureData, monument);
            databaseQuery.incrementTemperatureId(monument.getId());
            databaseQuery.destroy();
        }catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    public JSONObject getConsolidateMonumentData(Monument monument){
        DatabaseQuery databaseQuery = new DatabaseQuery();
        JSONObject object = new JSONObject();
        try {
            object.put("name", monument.getName());
            object.put("id", monument.getId());
            object.put("latitude", monument.getLatitude());
            object.put("longitude", monument.getLongitude());
            object.put("warningCount", databaseQuery.getNextWarningId(monument.getId()) - 1);
            object.put("postCount", databaseQuery.getNextPostId(monument.getId()) - 1);
            object.put("monumentPhoto", monument.getMonumentPhoto());
            object.put("custodian", monument.getCustodian());
            if(monument.getReference() != null) {
                object.put("reference", monument.getReference());
            }
            object.put("desc", monument.getDesc());
            databaseQuery.destroy();
        }catch (SQLException e){
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return object;

    }
}
