package com.nerisa.datarepo.model;

import com.nerisa.datarepo.utils.Constant;

/**
 * Created by nerisa on 3/15/18.
 */
public class User {

    private Long id;
    private String email;
    private String token;
    private Long lastLoggedIn;
    private boolean isCustodian;
    private long monumentId;

    public User(){}

    public User(String email, String token, Long lastLoggedIn) {
        this.email = email;
        this.token = token;
        this.lastLoggedIn = lastLoggedIn;
        this.isCustodian = Boolean.FALSE;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(Long lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public boolean isCustodian() {
        return isCustodian;
    }

    public void setCustodian(boolean isCustodian) {
        this.isCustodian = isCustodian;
    }

    public String createUri(){
        return Constant.USER_BASE_URI + "/" + id;
    }

    public long getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(long monumentId) {
        this.monumentId = monumentId;
    }
}
