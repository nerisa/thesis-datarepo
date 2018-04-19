package com.nerisa.datarepo.model;

import com.nerisa.datarepo.utils.Constant;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nerisa on 3/26/18.
 */
@XmlRootElement
public class TemperatureData{

    private Long id;
    private Double value;
    private Long date;

    public TemperatureData(){}

    public TemperatureData( Double value, Long date) {
        this.value = value;
        this.date = date;
    }


    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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


    public String retrieveTemperatureUri(Monument monument){
        return monument.getMonumentUri() + Constant.TEMPERATURE_BASE_URI + "/" + id;
    }
}
