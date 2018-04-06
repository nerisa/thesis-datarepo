package com.nerisa.datarepo.model;

import com.nerisa.datarepo.utils.Constant;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nerisa on 3/27/18.
 */
@XmlRootElement
public class NoiseData {

    private Long id;
    private String file;
    private Long date;

    public NoiseData(){}

    public NoiseData(String file, Long date) {
        this.file = file;
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String retrieveNoiseUri(Monument monument){
        return monument.getMonumentUri() + Constant.NOISE_BASE_URI + "/" + getId();
    }

    public String retrieveNoiseUri(Long monumentId){
        return Monument.createMonumentUri(monumentId) + Constant.NOISE_BASE_URI + "/" + getId();
    }
}
