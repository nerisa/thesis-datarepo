package com.nerisa.datarepo.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nerisa on 3/14/18.
 */
@XmlRootElement
public class Post {

    private String desc;
    private Long date;
    private Long id;
    private long userId;

    public Post(){}

    public Post(String desc, Long date){
        this.desc = desc;
        this.date = date;
    }

    public Post(Long id, String desc, Long date){
        this.id = id;
        this.desc = desc;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
