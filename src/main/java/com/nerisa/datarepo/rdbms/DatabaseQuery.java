package com.nerisa.datarepo.rdbms;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.User;
import com.nerisa.datarepo.model.Warning;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/28/18.
 */
public class DatabaseQuery {

    private static final Logger LOG = Logger.getLogger(DatabaseQuery.class.getName());
    private static Connection dbCon = ConnectionManger.getConnection();
    private static final String APP_DATA_TABLE = "app_data";
    private static final String USER_TABLE = "user";
    private static final String MONUMENT_TABLE = "monument_data";
    private static final String WARNING_TABLE = "unverified_warning";

    public static Long getLastMonumentId() throws SQLException{
        Long monumentId = 0l;
        String query = "SELECT monument_id FROM " + APP_DATA_TABLE;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                monumentId = resultSet.getLong("monument_id");
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return monumentId;
    }

    public static void incrementMonumentId() throws SQLException{
        Long monumentId = 0l;
        String query = "";
        if(getLastMonumentId() == 0l){
            query = "INSERT INTO " + APP_DATA_TABLE + "(monument_id) VALUES (1)";

        } else {
            query = "UPDATE " + APP_DATA_TABLE + " SET monument_id = monument_id+1";
        }
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not increment monument id");
                throw new SQLException("Could not increment monument id");
            }
        } finally {
            if (statement != null){ statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static void incrementTemperatureId(Long monumentId) throws  SQLException{
        String query = "UPDATE " + MONUMENT_TABLE + " SET temp_id = temp_id + 1 WHERE monument_id = " + monumentId;
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not increment temperature id for " + monumentId);
                throw new SQLException("Could not increment temperature id for " + monumentId);
            }
        }finally {
            if (statement != null){ statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static void incrementNoiseId(Long monumentId) throws SQLException{
        String query = "UPDATE " + MONUMENT_TABLE + " SET noise_id = noise_id + 1 WHERE monument_id = " + monumentId;
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not increment noise id for " + monumentId);
                throw new SQLException("Could not increment noise id for " + monumentId);
            }
        }finally {
            if (statement != null){ statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static void addMonumentResourceIds(Long monumentId, Long warningCount, Long postCount, Long temperatureCount, Long noiseCount) throws SQLException{
        String query = "INSERT INTO " + MONUMENT_TABLE + " (monument_id, temp_id, noise_id, warning_id, post_id) VALUES (?,?,?,?,?)";
        LOG.log(Level.INFO, "inserting ids for resources for monument "+ monumentId);
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            statement.setLong(1, monumentId);
            statement.setLong(2, temperatureCount);
            statement.setLong(3, noiseCount);
            statement.setLong(4, warningCount);
            statement.setLong(5, postCount);
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not insert resource ids for " + monumentId);
                throw new SQLException("Could not insert resource ids for " + monumentId);
            }
        }finally {
            if (statement != null){ statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }



    public static void main(String[] args){
//        try {
//            User user = getUser("st118437@ait.asia");
//            System.out.println(user.getId());
//
//            int score = getUserScore(user);
//            System.out.println(score);
//
//            List<User> users = getAllOldCustodians();
//            System.out.println(users.get(0).getId());
//            System.out.println(users.size());
//        }catch (SQLException e){
//            e.printStackTrace();
//        }

    }

    public static void incrementWarningId(Long monumentId) throws SQLException{

        String insertQuery = "INSERT INTO " + MONUMENT_TABLE + "(monument_id, warning_id) VALUES (?, ?)";
        String updateQuery = "UPDATE " + MONUMENT_TABLE + " SET warning_id = warning_id + 1 WHERE monument_id = ?";
        int result = 0;
        PreparedStatement ps = null;
        try {
            if (getNextWarningId(monumentId) == 0l) {
                ps = dbCon.prepareStatement(insertQuery);
                ps.setLong(1, monumentId);
                ps.setLong(2, 1l);
                result = ps.executeUpdate();
            } else {
                ps = dbCon.prepareStatement(updateQuery);
                ps.setLong(1, monumentId);
                result = ps.executeUpdate();
            }
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not increment warning id for " + monumentId);
                throw new SQLException("Could not increment warning id for " + monumentId);
            }
        }finally {
            if (ps != null){ ps.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static void incrementPostId(Long monumentId) throws SQLException{
        String insertQuery = "INSERT INTO " + MONUMENT_TABLE + "(monument_id, post_id) VALUES (?, ?)";
        String updateQuery = "UPDATE " + MONUMENT_TABLE + " SET post_id = post_id + 1 WHERE monument_id = ?";
        PreparedStatement ps = null;
        try {
            if (getNextPostId(monumentId) == 0l) {
                ps = dbCon.prepareStatement(insertQuery);
                ps.setLong(1, monumentId);
                ps.setLong(2, 1l);
                ps.executeUpdate();
            } else {
                ps = dbCon.prepareStatement(updateQuery);
                ps.setLong(1, monumentId);
                ps.executeUpdate();
            }
        }finally {
            if (ps != null){ ps.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static Long getNextTemperatureId(Long monumentId) throws SQLException{
        Long tempId = 0l;
        String selectQuery = "SELECT temp_id FROM " +  MONUMENT_TABLE + " WHERE monument_id = " + monumentId;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                tempId = resultSet.getLong("temp_id") + 1;
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return tempId;
    }

    public static Long getNextNoiseId(Long monumentId) throws SQLException{
        Long noiseId = 0l;
        String selectQuery = "SELECT noise_id FROM " +  MONUMENT_TABLE + " WHERE monument_id = " + monumentId;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                noiseId = resultSet.getLong("noise_id") + 1;
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return noiseId;
    }

    public static Long getNextPostId(Long monumentId) throws SQLException{
        Long postId = 0l;
        String selectQuery = "SELECT post_id FROM " +  MONUMENT_TABLE + " WHERE monument_id = " + monumentId;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                postId = resultSet.getLong("post_id") + 1;
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return postId;
    }


    public static Long getNextWarningId(Long monumentId) throws SQLException{
        Long warningId = 0l;
        String selectQuery = "SELECT warning_id FROM " +  MONUMENT_TABLE + " WHERE monument_id = " + monumentId;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                warningId = resultSet.getLong("warning_id") + 1;
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return warningId;
    }

    public static Warning saveWarning(Warning warning, Long monumentId) throws SQLException{
        String insertQuery = "INSERT INTO " + WARNING_TABLE + " (monument_id, image, description, date) VALUES (?,?,?,?)";
        LOG.log(Level.INFO, "Inserting a new unverified warning for monument "+ monumentId);
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = dbCon.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, monumentId);
            statement.setString(2, warning.getImage());
            statement.setString(3, warning.getDesc());
            statement.setLong(4, warning.getDate());
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not insert warning data for " + monumentId);
                throw new SQLException("Could not delete warning data for " + monumentId);
            }
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                warning.setId(generatedKeys.getLong(1));
            } else {
                LOG.log(Level.SEVERE, "No id obtained for warning data for monument " + monumentId);
                throw new SQLException("Creating warning failed, no ID obtained.");
            }
        } finally {
            if (statement != null){ statement.close(); }
            if (generatedKeys != null){ generatedKeys.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return warning;
    }

    public static void deleteWarning(Warning warning) throws SQLException{
        String deleteQuery = "DELETE FROM " + WARNING_TABLE +" WHERE id = " + warning.getId();
        LOG.log(Level.INFO, "Deleting warning as unverified "+ warning.getId());
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(deleteQuery);
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not delete warning data for " + warning.getId());
                throw new SQLException("Could not delete warning data for " + warning.getId());
            }
        }finally {
            if (statement != null){ statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
    }

    public static List<Warning> getWarnings(Monument monument) throws SQLException{
        List<Warning> warningList = new ArrayList<Warning>();
        String query = "SELECT * FROM " + WARNING_TABLE + " WHERE monument_id = " + monument.getId();
        LOG.log(Level.INFO, "Getting warnings: " + query);
        Statement statement = null;
        ResultSet result = null;
        try {
            statement = dbCon.createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                Warning warning = new Warning();
                warning.setId(result.getLong("id"));
                warning.setDesc(result.getString("description"));
                warning.setImage(result.getString("image"));
                warning.setDate(result.getLong("date"));
                warning.setVerified(Boolean.FALSE);
                warningList.add(warning);
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (result != null){ result.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return warningList;
    }

    public static User createUser(User user) throws SQLException{
        String query = "INSERT INTO " + USER_TABLE + "(email, token, is_custodian, last_logged_in) VALUES (?,?,?,?)";
        LOG.log(Level.INFO, "Creating new user "+ user.getEmail());
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = dbCon.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getToken());
            statement.setBoolean(3, Boolean.FALSE);
            statement.setLong(4, user.getLastLoggedIn());
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not insert user data for " + user.getEmail());
                throw new SQLException("Could not insert user data for " + user.getEmail());
            }
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            } else {
                LOG.log(Level.SEVERE, "No id obtained for user " + user.getEmail());
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        } finally {
            if (statement != null){ statement.close(); }
            if (generatedKeys != null){ generatedKeys.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return user;
    }



    public static boolean updateUserToken(User user)throws SQLException{
        LOG.log(Level.INFO, "Updating user token for user: " + user.getEmail());
        String query = "UPDATE " + USER_TABLE + " SET token = ? WHERE id = ?";
        LOG.log(Level.INFO, "Updating user with query: " + query);
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            statement.setString(1, user.getToken());
            statement.setLong(2, user.getId());
            LOG.log(Level.INFO, "user token update statement: " + statement.toString());
            boolean result = statement.execute();
            if (!result) {
                LOG.log(Level.SEVERE, "Could not update user token for " + user.getEmail());
                throw new SQLException("Could not update user token for " + user.getEmail());
            }
        } finally {
            if (statement != null) { statement.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return Boolean.TRUE;
    }

    public static User getUser(Long id) throws SQLException{
        User user = new User();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE id = " + id;
        LOG.log(Level.INFO, "Getting user with query: " + query);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                user.setId(id);
                user.setEmail(resultSet.getString("email"));
                user.setToken(resultSet.getString("token"));
                user.setCustodian(resultSet.getBoolean("is_custodian"));
                user.setMonumentId(resultSet.getLong("monument_id"));
            }
        }finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return user;
    }

    public static User getUser(String email) throws SQLException{
        User user = new User();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE email = '" + email + "'";
        LOG.log(Level.INFO, "Getting user with query: " + query);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setToken(resultSet.getString("token"));
                user.setCustodian(resultSet.getBoolean("is_custodian"));
                user.setMonumentId(resultSet.getLong("monument_id"));
            }
        } finally {
            if (statement != null){ statement.close(); }
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return user;
    }


    public static boolean changeCustodianStatus(boolean status, User user) throws SQLException{
        LOG.log(Level.INFO, "Updating user custodian status to " + status + " for user: " + user.getEmail());
        String query = "UPDATE " + USER_TABLE + " SET is_custodian = ? WHERE id = ?";
        PreparedStatement statement = null;
        try{
            statement = dbCon.prepareStatement(query);
            statement.setBoolean(1, status);
            statement.setLong(2, user.getId());
            int result = statement.executeUpdate();
            if(result == 0){
                LOG.log(Level.SEVERE, "Could not update user status for " + user.getEmail());
                throw new SQLException("Could not update user status for " + user.getEmail());
            }
        } finally {
            if (statement != null){ statement.close(); }
        }
        return Boolean.TRUE;
    }

    public static boolean updateMonumentId(User user, long monumentId) throws SQLException{
        LOG.log(Level.INFO, "Updating user monument_id to " + monumentId + " for user: " + user.getEmail());
        String query = "UPDATE " + USER_TABLE + " SET monument_id = ? WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = dbCon.prepareStatement(query);
            statement.setLong(1, monumentId);
            statement.setLong(2, user.getId());
            int result = statement.executeUpdate();
            if (result == 0) {
                LOG.log(Level.SEVERE, "Could not update user status for " + user.getEmail());
                throw new SQLException("Could not update user status for " + user.getEmail());
            }
        }finally {
            if (statement != null) { statement.close();}
        }
        return Boolean.TRUE;
    }

    public static int updateUserScore(User user, int score) throws SQLException{
        LOG.log(Level.INFO, "Updating user score: " + user.getId() + "," + score);
        int previousScore = getUserScore(user);
        String query = "UPDATE " + USER_TABLE + " SET score = ? WHERE id = ?";

        PreparedStatement updateStatement = null;
        try{
            updateStatement = dbCon.prepareStatement(query);
            updateStatement.setInt(1,(previousScore + score));
            updateStatement.setLong(2, user.getId());
            int result = updateStatement.executeUpdate();
            if(result == 0){
                LOG.log(Level.SEVERE, "Could not update user status for " + user.getEmail());
                throw new SQLException("Could not update user status for " + user.getEmail());
            }
        } finally {
            if (updateStatement != null) { updateStatement.close(); }
        }

        return (previousScore+score);
    }

    public static int getUserScore(User user) throws SQLException{
        LOG.log(Level.INFO, "Retrieving user score: " + user.getId());
        String selectQuery = "SELECT score FROM " + USER_TABLE + " WHERE id = " + user.getId();
        int userScore = 0;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                userScore = resultSet.getInt("score");
            }
        }finally {
            if(statement!=null)
                statement.close();
        }
        return userScore;
    }

    public static List<User> getAllOldCustodians() throws SQLException{
//        Long oneWeekBefore = (System.currentTimeMillis() - (Constant.OLD_DATA_DAYS * Constant.DAY_IN_MS));
        Long oneWeekBefore = System.currentTimeMillis();
        List<User> userList = new ArrayList<User>();
        String query = "SELECT * FROM " + USER_TABLE + " WHERE (last_logged_in < " + oneWeekBefore + ") AND (is_custodian = 1)";
        LOG.log(Level.INFO, "Getting old custodians query: " + query);
        ResultSet resultSet = null;
        Statement statement = null;
        try{
            statement = dbCon.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setCustodian(Boolean.TRUE);
                user.setMonumentId(resultSet.getLong("monument_id"));
                user.setToken(resultSet.getString("token"));
                user.setLastLoggedIn(resultSet.getLong("last_logged_in"));
                user.setEmail(resultSet.getString("email"));
                userList.add(user);
            }
        }finally {
            if (statement != null) { statement.close();}
            if (resultSet != null){ resultSet.close(); }
//            if (dbCon != null){ dbCon.close(); }
        }
        return userList;
    }
}
