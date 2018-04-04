package com.nerisa.datarepo.model;

import com.nerisa.datarepo.utils.Constant;
import org.json.simple.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by nerisa on 3/13/18.
 */
@XmlRootElement
public class Warning {

    private String desc;
    private String image;
    private Long date;
    private boolean isVerified;
    private Long id;
    private long userId;

    public Warning(){}

    public Warning(String desc, String image, Long date){
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.isVerified = false;
    }

    public Warning(Long id, String desc, String image, Long date, boolean isVerified){
        this.id = id;
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.isVerified = isVerified;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void verify(){
        this.isVerified = true;
    }

    public boolean isVerified(){
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public JSONObject prepareNotificationBody(Long monumentId){
        JSONObject warningNotification = new JSONObject();
        warningNotification.put("monument", monumentId.toString());
        warningNotification.put("id", id);
        warningNotification.put("type", "warning");
        warningNotification.put("timestamp", date);
        return warningNotification;
    }

    public String createWarningUri(Monument monument){
        return Constant.BASE_URI + "/" + monument.getId() + Constant.WARNING_BASE_URI + "/" + getId();
    }
}
